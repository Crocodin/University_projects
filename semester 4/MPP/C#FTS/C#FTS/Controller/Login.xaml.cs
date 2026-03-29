using C_FTS.Domain;
using C_FTS.Service;
using C_FTS.Service.Authenticator;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace C_FTS.Controller
{
    public partial class Login : Window
    {
        private readonly IAuthenticator authenticator = new Authenticator();
        private readonly IFestivalService festivalService = new FestivalService();
        public Login()
        {
            InitializeComponent();
        }
        private void IfIsEnterUsername(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                password.Focus();
            }
        }
        private void IfIsEnterPassword(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                Login_Cliked();
            }
        }
        private void LoginBtn(object sender, RoutedEventArgs e)
        {
            Login_Cliked();
        }
        private void Login_Cliked()
        {
            var user = authenticator.Authenticate(username.Text, password.Password);
            if (user != null)
            {
                OpenMainWindow(user);
            }
            else ShowError();
        }
        private void OpenMainWindow(User user)
        {
            var mainWindow = new MainWindow(festivalService, user);
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
