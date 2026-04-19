using C_FTS.Domain;
using FTS.Networking.Networking.Service;
using log4net;
using System.Windows;
using System.Windows.Input;

namespace C_FTS.Controller
{
    public partial class Login : Window
    {
        private readonly FestivalServicesJsonProxy _server;
        private static readonly ILog logger = LogManager.GetLogger(typeof(Login));

        public Login(FestivalServicesJsonProxy server)
        {
            InitializeComponent();
            _server = server;
        }

        private void IfIsEnterUsername(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
                password.Focus();
        }

        private void IfIsEnterPassword(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
                LoginClicked();
        }

        private void LoginBtn(object sender, RoutedEventArgs e)
        {
            LoginClicked();
        }

        private void LoginClicked()
        {
            User? user = _server.Authenticate(username.Text, password.Password);
            if (user != null)
                OpenMainWindow(user);
            else
                ShowError();
        }

        private void OpenMainWindow(User user)
        {
            _server.Login(user.Username, password.Password, /* observer */ null);
            var mainWindow = new MainWindow(_server, user);
            _server.SetObserver(mainWindow);
            mainWindow.Title = "Login as " + user.Username;
            mainWindow.Show();
            this.Close();
        }

        private void ShowError()
        {
            MessageBox.Show("Invalid username or password", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }
}