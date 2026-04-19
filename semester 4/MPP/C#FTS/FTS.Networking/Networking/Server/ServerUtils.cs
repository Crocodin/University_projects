using log4net;
using System.Net;
using System.Net.Sockets;

namespace C_FTS.Networking.Server
{
    public abstract class AbstractServer
    {
        private TcpListener _server;
        private String _host;
        private int _port;

        private static readonly ILog logger = LogManager.GetLogger(typeof(AbstractServer));
        public AbstractServer(String host, int port)
        {
            this._host = host;
            this._port = port;
        }

        public void Start()
        {
            logger.InfoFormat("Starting server on {0}:{1}", _host, _port);
            IPAddress iPAddress = IPAddress.Parse(_host);
            IPEndPoint endPoint = new IPEndPoint(iPAddress, _port);

            _server = new TcpListener(endPoint);
            _server.Start();
            logger.Info("Server started successfully.");

            while (true)
            {
                logger.Debug("Waiting for client connection...");
                TcpClient client = _server.AcceptTcpClient();
                logger.InfoFormat("Client connected: {0}", client.Client.RemoteEndPoint);
                this.ProcessRequest(client);
            }
        }

        public abstract void ProcessRequest(TcpClient client);
        public void Stop()
        {
            logger.Info("Stopping server...");
            _server.Stop();
            logger.Info("Server stopped successfully.");
        }
    }

    public abstract class ConcurrentServer : AbstractServer
    {
        public ConcurrentServer(string host, int port) : base(host, port) { }
        public override void ProcessRequest(TcpClient client)
        {
            Thread clientThread = new Thread(() => HandleClient(client));
            clientThread.Start();
        }
        protected abstract void HandleClient(TcpClient client);
    }
}
