using C_FTS.Domain;
using C_FTS.Networking.DTO;
using C_FTS.Networking.Protocol;

using log4net;

using System.Net.Sockets;
using System.Text;
using System.Text.Json;

namespace FTS.Networking.Networking.Service
{
    public class FestivalClientJsonWorker : IFestivalObserver
    {
        private readonly IFestivalSubject _festivalService;
        private readonly TcpClient _client;

        private readonly NetworkStream _stream;
        private readonly StreamReader _reader;
        private readonly StreamWriter _writer;
        private volatile bool _running;

        private static readonly ILog logger = LogManager.GetLogger(typeof(FestivalClientJsonWorker));

        public FestivalClientJsonWorker(IFestivalSubject festivalService, TcpClient client)
        {
            logger.Debug("Creating FestivalClientJsonWorker");
            this._festivalService = festivalService;
            this._client = client;

            try
            {
                this._stream = client.GetStream();
                this._reader = new StreamReader(_stream, Encoding.UTF8);
                this._writer = new StreamWriter(_stream, Encoding.UTF8) { AutoFlush = true };
                this._running = true;
            }
            catch (Exception ex)
            {
                logger.Error("Error initializing Json Worker", ex);
            }
        }

        public void Run()
        {
            while (_running)
            {
                try
                {
                    string requestLine = _reader.ReadLine();
                    if (requestLine == null)
                    {
                        _running = false;
                        break;
                    }

                    logger.DebugFormat("Request received: {0}", requestLine);
                    Request request = JsonSerializer.Deserialize<Request>(requestLine);
                    Response response = HandleRequest(request);

                    if (response != null)
                        SendResponse(response);
                }
                catch (IOException ex)
                {
                    logger.Error("Error reading request", ex);
                    _running = false;
                }
            }
            CloseConnection();
        }

        private Response HandleRequest(Request request)
        {
            logger.DebugFormat("Handling request of type: {0}", request.Type);

            switch (request.Type)
            {
                case RequestType.LOGIN:
                    {
                        UserDTO userDTO = request.User;
                        logger.DebugFormat("Login request from: {0}", userDTO.Username);

                        var user = _festivalService.Authenticate(userDTO.Username, userDTO.Password);
                        if (user == null)
                        {
                            return new Response (
                                type: ResponseType.ERROR,
                                message: "User not found!"
                            );
                        }
                        _festivalService.Login(userDTO.Username, userDTO.Password, this);
                        return new Response(
                            type: ResponseType.OK, 
                            user: userDTO
                        );
                    }

                case RequestType.LOGOUT:
                    {
                        logger.Debug("Logout request");
                        _festivalService.Logout(request.User.Username);
                        _running = false;
                        return new Response(type: ResponseType.OK);
                    }

                case RequestType.FIND_ALL:
                    {
                        var all = _festivalService.FindAll();
                        ShowArtistDTO[] dtos = all.Select(DTOUtil.GetDTO).ToArray();
                        return new Response (
                            type: ResponseType.FIND_ALL,
                            showArtist: dtos
                        );
                    }

                case RequestType.FIND_BY_DATE:
                    {
                        DateTime date = DateTime.Parse(request.Date);
                        var filtered = _festivalService.FindByDate(date);
                        ShowArtistDTO[] dtos = filtered.Select(DTOUtil.GetDTO).ToArray();
                        return new Response(
                            type: ResponseType.FIND_BY_DATE,
                            showArtist: dtos
                        );
                    }

                case RequestType.BUY_TICKET:
                    {
                        TicketDTO ticketDTO = request.Ticket;
                        Show show = DTOUtil.GetFromDTO(ticketDTO.Show);

                        Ticket? result = _festivalService.SellTicket(show, ticketDTO.BuyerName, ticketDTO.NumberOfSeats);
                        if (result != null)
                        {
                            return new Response (
                                type: ResponseType.OK,
                                ticket: DTOUtil.GetDTO(result)
                            );
                        }
                        return new Response (
                            type: ResponseType.ERROR,
                            message: "Could not sell ticket"
                        );
                    }

                case RequestType.MODIFY_TICKET:
                    {
                        if (_festivalService.ModifyTicket(request.TicketId, request.Seats))
                        {
                            return new Response(type: ResponseType.OK);
                        }
                        return new Response (
                            type: ResponseType.ERROR,
                            message: "Ticket not found"
                        );
                    }

                default:
                    logger.WarnFormat("Unknown request type: {0}", request.Type);
                    return new Response(
                        type: ResponseType.ERROR,
                        message: "Unknown request type"
                    );
            }
        }

        private void SendResponse(Response response)
        {
            string json = JsonSerializer.Serialize(response);
            logger.DebugFormat("Sending response: {0}", response.Type.ToString());
            lock (_writer)
            {
                _writer.WriteLine(json);
            }
        }

        private void CloseConnection()
        {
            try
            {
                _reader.Close();
                _writer.Close();
                _stream.Close();
                _client.Close();
                logger.Debug("Connection closed");
            }
            catch (IOException ex)
            {
                logger.Error("Error closing connection", ex);
            }
        }

        public void TicketSold(Ticket ticket)
        {
            SendResponse(new Response(
                type: ResponseType.TICKET_SOLD,
                ticket: DTOUtil.GetDTO(ticket)
            ));
        }

        public void TicketModified(Ticket ticket)
        {
            SendResponse(new Response (
                type: ResponseType.TICKET_MODIFIED,
                ticket: DTOUtil.GetDTO(ticket)
            ));
        }
    }
}