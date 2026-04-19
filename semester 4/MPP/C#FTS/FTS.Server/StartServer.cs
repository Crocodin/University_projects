using C_FTS.Repository.DBRepository;
using C_FTS.Service;
using C_FTS.Service.Authenticator;
using FTS.Networking.Networking.Server;
using FTS.Networking.Networking.Service;
using FTS.Server;
using log4net;
using System.Configuration;
using System.Runtime.CompilerServices;

namespace FTS.Server
{
    internal class StartServer
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(StartServer));

        private static readonly ShowRepository _showRepository = new ShowRepository();
        private static readonly TicketRepository _ticketRepository = new TicketRepository();
        private static readonly ShowArtistRepository _showArtistRepository = new ShowArtistRepository();
        private static readonly IAuthenticator _authenticator = new Authenticator();

        static void Main(string[] args)
        {
            log4net.Config.XmlConfigurator.Configure(new FileInfo("log4net.xml"));
            logger.Info("Starting FTS Json - C# Server");
            try
            {
                string host = ConfigurationManager.AppSettings["Server.Host"]!;
                int port = int.Parse(ConfigurationManager.AppSettings["Server.Port"]!);

                IFestivalSubject festivalService = new FestivalCSService(
                    _showRepository, _ticketRepository, _showArtistRepository, _authenticator
                );
                FestivalJsonCS server = new FestivalJsonCS(host, port, festivalService);

                server.Start();
            }
            catch (Exception ex)
            {
                logger.ErrorFormat("Error starting server: {0}", ex.Message);
            }
        }
    }
}