using C_FTS.Controller;
using FTS.Networking.Networking.Service;
using log4net;
using System.Configuration;
using System.IO;
using System.Reflection;
using System.Windows;

namespace C_FTS
{
    public partial class App : Application
    {
        private FestivalServicesGrpcProxy? _server;
        private static readonly ILog logger = LogManager.GetLogger(typeof(App));

        protected override void OnStartup(StartupEventArgs e)
        {
            var logRepo = LogManager.GetRepository(Assembly.GetEntryAssembly());
            log4net.Config.XmlConfigurator.Configure(logRepo, new FileInfo("log4net.xml"));
            base.OnStartup(e);
            logger.Info("Starting FestivalClient");

            string host = ConfigurationManager.AppSettings["Server.Host"]!;
            int port = int.Parse(ConfigurationManager.AppSettings["Server.Port"]!);

            logger.DebugFormat("Host: {0}", host);
            logger.DebugFormat("Port: {0}", port);

            _server = new FestivalServicesGrpcProxy(host, port);
            _server.Start();

            Login loginWindow = new Login(_server);
            loginWindow.Show();
        }

        protected override void OnExit(ExitEventArgs e)
        {
            logger.Info("Application shutting down, closing resources");
            _server?.Logout("");
            logger.Info("Resources closed successfully");
            base.OnExit(e);
        }
    }
}
