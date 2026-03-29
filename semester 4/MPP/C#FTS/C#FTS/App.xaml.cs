using C_FTS.Controller;
using C_FTS.Repository.DBRepository;
using C_FTS.Service;
using C_FTS.Service.Authenticator;
using C_FTS.Utils;
using log4net;
using log4net.Config;
using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace C_FTS
{
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            var logRepo = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepo, new FileInfo("log4net.config"));

            base.OnStartup(e);
            var loginWindow = new Login();
            loginWindow.Title = "FTS - Login";
            loginWindow.Show();
        }
    }
}
