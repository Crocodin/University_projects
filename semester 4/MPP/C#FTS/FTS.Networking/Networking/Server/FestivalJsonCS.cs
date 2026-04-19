using C_FTS.Networking.Server;

using FTS.Networking.Networking.Service;

using log4net;
using System.Net.Sockets;

namespace FTS.Networking.Networking.Server
{
    public class FestivalJsonCS : ConcurrentServer
    {
        private readonly IFestivalSubject _festivalService;
        private static readonly ILog logger = LogManager.GetLogger(typeof(FestivalJsonCS));

        public FestivalJsonCS(string host, int port, IFestivalSubject festivalService) : base(host, port)
        {
            logger.Info("Creating FestivalJsonCS");
            this._festivalService = festivalService;
            logger.Info("Created FestivalJsonCS successfully");
        }

        protected override void HandleClient(TcpClient client)
        {
            logger.InfoFormat("Handling client: {0}", client.Client.RemoteEndPoint);
            FestivalClientJsonWorker worker = new FestivalClientJsonWorker(this._festivalService, client);
            worker.Run();
        }
    }
}