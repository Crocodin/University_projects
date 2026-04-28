using C_FTS.Domain;
using C_FTS.Service;
using FestivalGrpc;
using Google.Protobuf.WellKnownTypes;
using Grpc.Core;
using Grpc.Net.Client;
using log4net;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace FTS.Networking.Networking.Service
{
    public class FestivalServicesGrpcProxy : IFestivalSubject
    {
        private readonly string _host;
        private readonly int _port;

        private IFestivalObserver? _client;
        private GrpcChannel? _channel;
        private FestivalGrpc.FestivalService.FestivalServiceClient? _grpcClient;

        private string? _loggedUsername;
        private CancellationTokenSource _cts = new();

        private static readonly ILog logger = LogManager.GetLogger(typeof(FestivalServicesGrpcProxy));

        public FestivalServicesGrpcProxy(string host, int port)
        {
            _host = host;
            _port = port;
        }

        public void Start()
        {
            // gRPC by default uses HTTP/2 + TLS, but for local dev we use insecure
            _channel = GrpcChannel.ForAddress($"http://{_host}:{_port}", new GrpcChannelOptions
            {
                Credentials = ChannelCredentials.Insecure
            });
            _grpcClient = new FestivalGrpc.FestivalService.FestivalServiceClient(_channel);
            logger.Info($"gRPC channel opened to {_host}:{_port}");
        }

        public void Stop()
        {
            _cts.Cancel();
            _channel?.Dispose();
            logger.Info("gRPC channel closed");
        }

        public User? Authenticate(string username, string password)
        {
            logger.Info($"Attempting login for user: {username}");

            try
            {
                var response = _grpcClient!.Login(new LoginRequest
                {
                    Username = username,
                    Password = password
                });

                if (response.Success)
                {
                    logger.Info($"Login successful for user: {username} (id={response.UserId})");
                    _loggedUsername = username;
                    return new User(response.UserId, response.Username, password);
                }

                logger.Warn($"Login failed for user: {username} - {response.ErrorMessage}");
                throw new ServerErrorStatus(response.ErrorMessage);
            }
            catch (Exception ex)
            {
                logger.Error($"Exception during login for user: {username}", ex);
                throw;
            }
        }

        public void Logout(string username)
        {
            logger.Info($"Logout requested for user: {_loggedUsername ?? username}");

            try
            {
                _grpcClient!.Logout(new LogoutRequest { Username = _loggedUsername ?? username });
                logger.Info("Logout successful");
            }
            catch (Exception ex)
            {
                logger.Error("Error during logout", ex);
            }
            finally
            {
                Stop();
            }
        }

        // needed by IFestivalSubject but not used — Authenticate does the work
        public void Login(string username, string password, IFestivalObserver observer) { }

        public void SetObserver(IFestivalObserver observer)
        {
            _client = observer;
            // start the subscribe stream now that we have an observer to push to
            StartSubscribeStream();
        }

        public List<ShowArtist> FindAll()
        {
            logger.Debug("Calling FindAll");

            try
            {
                var response = _grpcClient!.FindAll(new Empty());
                logger.Debug($"FindAll returned {response.ShowArtists.Count} entries");

                return response.ShowArtists.Select(ProtoToDomain).ToList();
            }
            catch (Exception ex)
            {
                logger.Error("Error in FindAll", ex);
                throw;
            }
        }

        public List<ShowArtist> FindByDate(DateTime date)
        {
            var formattedDate = date.ToString("yyyy-MM-dd");
            logger.Debug($"Calling FindByDate with date={formattedDate}");

            try
            {
                var response = _grpcClient!.FindByDate(new DateRequest
                {
                    Date = formattedDate
                });

                logger.Debug($"FindByDate returned {response.ShowArtists.Count} entries");

                return response.ShowArtists.Select(ProtoToDomain).ToList();
            }
            catch (Exception ex)
            {
                logger.Error($"Error in FindByDate for date={formattedDate}", ex);
                throw;
            }
        }

        public Ticket? SellTicket(Show show, string buyerName, int seats)
        {
            logger.Info($"SellTicket request: showId={show.Id}, buyer={buyerName}, seats={seats}");

            try
            {
                var response = _grpcClient!.SellTicket(new SellTicketRequest
                {
                    ShowId = show.Id,
                    BuyerName = buyerName,
                    Seats = seats
                });

                if (response.Success)
                {
                    logger.Info($"Ticket sold successfully: ticketId={response.TicketId}");
                    return new Ticket(response.TicketId, buyerName, seats, show);
                }

                logger.Warn($"SellTicket failed: {response.ErrorMessage}");
                throw new ProxyErrorStatus(response.ErrorMessage);
            }
            catch (Exception ex)
            {
                logger.Error("Error during SellTicket", ex);
                throw;
            }
        }

        public bool ModifyTicket(int ticketId, int seats)
        {
            logger.Info($"ModifyTicket request: ticketId={ticketId}, seats={seats}");

            try
            {
                var response = _grpcClient!.ModifyTicket(new ModifyTicketRequest
                {
                    TicketId = ticketId,
                    Seats = seats
                });

                if (response.Success)
                {
                    logger.Info("ModifyTicket successful");
                    return true;
                }

                logger.Warn($"ModifyTicket failed: {response.ErrorMessage}");
                throw new ProxyErrorStatus(response.ErrorMessage);
            }
            catch (Exception ex)
            {
                logger.Error("Error during ModifyTicket", ex);
                throw;
            }
        }

        private void StartSubscribeStream()
        {
            if (_grpcClient == null)
            {
                logger.Error("_grpcClient is null! Start() was not called.");
                return;
            }

            logger.Info($"Starting subscribe stream for user={_loggedUsername}");

            _cts = new CancellationTokenSource();

            Task.Run(async () =>
            {
                try
                {
                    using var stream = _grpcClient.Subscribe(new SubscribeRequest
                    {
                        Username = _loggedUsername ?? ""
                    });

                    logger.Info("Subscribe stream opened");

                    await foreach (var eventMsg in stream.ResponseStream.ReadAllAsync(_cts.Token))
                    {
                        logger.Debug($"Event received: type={eventMsg.EventType}, ticketId={eventMsg.TicketId}, showId={eventMsg.Show.ShowId}");
                        HandleEvent(eventMsg);
                    }
                }
                catch (RpcException ex) when (ex.StatusCode == StatusCode.Cancelled)
                {
                    logger.Info("Subscribe stream cancelled (normal on logout)");
                }
                catch (Exception ex)
                {
                    logger.Error("Error in subscribe stream", ex);
                }
            });
        }

        private void HandleEvent(EventMessage msg)
        {
            if (_client == null)
            {
                logger.Warn("Received event but no client observer is set");
                return;
            }

            logger.Debug($"Handling event: {msg.EventType}, showId={msg.Show.ShowId}");

            var protoShow = msg.Show;
            var protoVenue = protoShow.Venue;

            var venue = new Venue(
                protoVenue.Id,
                protoVenue.Name,
                protoVenue.Address,
                protoVenue.Capacity
            );

            var show = new Show(
                protoShow.ShowId,
                protoShow.Date,
                protoShow.Title,
                protoShow.SoldSeats,
                venue
            );

            var ticket = new Ticket(
                msg.TicketId,
                "", // buyer unknown in event
                msg.Seats,
                show
            );

            switch (msg.EventType)
            {
                case EventType.TicketSold:
                    logger.Debug("Dispatching TicketSold event");
                    _client.TicketSold(ticket);
                    break;

                case EventType.TicketModified:
                    logger.Debug("Dispatching TicketModified event");
                    _client.TicketModified(ticket);
                    break;

                default:
                    logger.Warn($"Unknown event type: {msg.EventType}");
                    break;
            }
        }

        private static ShowArtist ProtoToDomain(ShowArtistMessage msg)
        {
            logger.Debug($"Mapping ShowArtist: showId={msg.Show.ShowId}, artist={msg.Artist.Name}");

            var protoArtist = msg.Artist;
            var protoShow = msg.Show;
            var protoVenue = protoShow.Venue;

            var venue = new Venue(
                protoVenue.Id,
                protoVenue.Name,
                protoVenue.Address,
                protoVenue.Capacity
            );

            var show = new Show(
                protoShow.ShowId,
                protoShow.Date,
                protoShow.Title,
                protoShow.SoldSeats,
                venue
            );

            var artist = new Artist(
                protoArtist.Id,
                protoArtist.Name
            );

            return new ShowArtist(show, artist);
        }
    }
}