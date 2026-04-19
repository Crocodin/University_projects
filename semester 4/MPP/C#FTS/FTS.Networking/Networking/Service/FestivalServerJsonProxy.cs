using C_FTS.Domain;
using C_FTS.Networking.DTO;
using C_FTS.Networking.Protocol;

using log4net;

using System.Collections.Concurrent;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;

namespace FTS.Networking.Networking.Service
{
    public class ServerErrorStatus : Exception
    {
        public ServerErrorStatus(string message) : base(message) { }
    }

    public class ProxyErrorStatus : Exception
    {
        public ProxyErrorStatus(string message) : base(message) { }
    }

    public class FestivalServicesJsonProxy : IFestivalSubject
    {
        private readonly string _host;
        private readonly int _port;

        private IFestivalObserver? _client;

        private StreamReader _reader;
        private StreamWriter _writer;
        private TcpClient _connection;

        private readonly BlockingCollection<Response> _responseQueue = new();
        private volatile bool _running;

        private static readonly ILog logger = LogManager.GetLogger(typeof(FestivalServicesJsonProxy));

        public FestivalServicesJsonProxy(string host, int port)
        {
            _host = host;
            _port = port;
        }

        public void Start() => InitializeConnection();

        public void Stop() => CloseConnection();

        public User? Authenticate(string username, string password)
        {
            SendRequest(new Request (
                type: RequestType.LOGIN,
                user: new UserDTO(id: -1, username: username, password: password)
            ));

            Response response = ReadResponse();

            if (response.Type == ResponseType.OK)
                return DTOUtil.GetFromDTO(response.User);

            if (response.Type == ResponseType.ERROR)
            {
                logger.ErrorFormat("Login Failed - closing connection - {0}", response.Message);
                CloseConnection();
                throw new ServerErrorStatus(response.Message);
            }

            return null;
        }

        public void Logout(string username)
        {
            logger.Debug("JsonProxy request logged out");
            SendRequest(new Request(type: RequestType.LOGOUT));

            Response response = ReadResponse();
            Stop();

            if (response.Type == ResponseType.ERROR)
            {
                logger.ErrorFormat("JsonProxy error logging out: {0}", response.Message);
                throw new ServerErrorStatus(response.Message);
            }
        }

        public List<ShowArtist> FindByDate(DateTime date)
        {
            SendRequest(new Request(
                type: RequestType.FIND_BY_DATE,
                date: date.ToString()
            ));

            Response response = ReadResponse();

            if (response.Type == ResponseType.FIND_BY_DATE)
                return response.ShowArtist.Select(DTOUtil.GetFromDTO).ToList();

            return [];
        }

        public List<ShowArtist> FindAll()
        {
            SendRequest(new Request(type: RequestType.FIND_ALL));

            Response response = ReadResponse();

            if (response.Type == ResponseType.FIND_ALL)
                return response.ShowArtist.Select(DTOUtil.GetFromDTO).ToList();

            return [];
        }

        public Ticket? SellTicket(Show show, string buyerName, int seats)
        {
            SendRequest(new Request(
                type: RequestType.BUY_TICKET,
                ticket: new TicketDTO (
                    buyerName: buyerName,
                    numberOfSeats: seats,
                    show: DTOUtil.GetDTO(show)
                )
            ));

            Response response = ReadResponse();

            if (response.Type == ResponseType.OK)
                return DTOUtil.GetFromDTO(response.Ticket);

            throw new ProxyErrorStatus(response.Message);
        }

        public bool ModifyTicket(int ticketId, int seats)
        {
            SendRequest(new Request(
                type: RequestType.MODIFY_TICKET,
                ticketId: ticketId,
                seats: seats
            ));

            Response response = ReadResponse();

            if (response.Type == ResponseType.OK)
                return true;

            throw new ProxyErrorStatus(response.Message);
        }

        public void Login(string username, string password, IFestivalObserver observer)
        {
            logger.Info("I should never print");
        }

        public void SetObserver(IFestivalObserver observer)
        {
            _client = observer;
        }

        private void InitializeConnection()
        {
            try
            {
                _connection = new TcpClient(_host, _port);
                var stream = _connection.GetStream();
                _reader = new StreamReader(stream, Encoding.UTF8);
                _writer = new StreamWriter(stream, Encoding.UTF8) { AutoFlush = true };
                _running = true;

                StartReaderThread();
            }
            catch (Exception ex)
            {
                logger.Error("Error initializing connection in JsonProxy", ex);
                throw;
            }
        }

        private void StartReaderThread()
        {
            var thread = new Thread(ReaderThreadLoop) { IsBackground = true };
            thread.Start();
        }

        private void ReaderThreadLoop()
        {
            while (_running)
            {
                try
                {
                    string? responseLine = _reader.ReadLine();
                    if (responseLine == null) break;

                    Response response = JsonSerializer.Deserialize<Response>(responseLine)!;
                    logger.DebugFormat("Received response: {0}", response.Type);

                    if (IsUpdate(response.Type))
                        HandleUpdate(response);
                    else
                        _responseQueue.Add(response);
                }
                catch (IOException ex)
                {
                    logger.Error("IOException in ReaderThread", ex);
                    break;
                }
            }
        }

        private bool IsUpdate(ResponseType type) =>
            type == ResponseType.TICKET_SOLD || type == ResponseType.TICKET_MODIFIED;

        private void HandleUpdate(Response response)
        {
            if (_client == null) return;

            switch (response.Type)
            {
                case ResponseType.TICKET_SOLD:
                    {
                        Ticket ticket = DTOUtil.GetFromDTO(response.Ticket);
                        logger.DebugFormat("Received ticket sold update: {0}", ticket);
                        try { _client.TicketSold(ticket); }
                        catch (Exception ex) { logger.ErrorFormat("Error notifying observer for ticket sold: {0}", ex.Message); }
                        break;
                    }
                case ResponseType.TICKET_MODIFIED:
                    {
                        Ticket ticket = DTOUtil.GetFromDTO(response.Ticket);
                        logger.DebugFormat("Received ticket modified update: {0}", ticket);
                        try { _client.TicketModified(ticket); }
                        catch (Exception ex) { logger.ErrorFormat("Error notifying observer for ticket modified: {0}", ex.Message); }
                        break;
                    }
                default:
                    logger.WarnFormat("Received unexpected update type: {0}", response.Type);
                    break;
            }
        }

        private void SendRequest(Request request)
        {
            string json = JsonSerializer.Serialize(request);
            logger.DebugFormat("Sending request: {0}", json);
            try
            {
                lock (_writer)
                {
                    _writer.WriteLine(json);
                }
            }
            catch (Exception ex)
            {
                logger.Error("Error sending request in JsonProxy", ex);
                throw;
            }
        }

        private Response ReadResponse()
        {
            try
            {
                return _responseQueue.Take();
            }
            catch (Exception ex)
            {
                logger.Error("Error reading from response queue", ex);
                throw;
            }
        }

        private void CloseConnection()
        {
            _running = false;
            _responseQueue.CompleteAdding();
            try
            {
                _reader?.Close();
                _writer?.Close();
                _connection?.Close();
            }
            catch (IOException ex)
            {
                logger.Error("Error closing connection", ex);
            }
        }
    }
}