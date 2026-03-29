using C_FTS.Domain;
using C_FTS.Service;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;
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
    public partial class MainWindow : Window
    {
        private readonly IFestivalService _festivalService;
        private readonly User User;

        private ShowArtist? _selectedShowArtist;
        private DateTime? _selectedDate;

        private readonly ObservableCollection<ShowArtist> _fullShowArtistList = new();
        private readonly ObservableCollection<ShowArtist> _filteredShowArtistList = new();

        private const string DATE_FORMAT = "dd/MM/yyyy";
        private const string TIME_FORMAT = "HH:mm";
        public MainWindow(IFestivalService festivalService, User user)
        {
            InitializeComponent();
            this._festivalService = festivalService;
            this.User = user;

            SetupTables();
            SetupKeyBindings();
            Refresh();
        }

        private void SetupTables()
        {
            fullTableView.ItemsSource = _fullShowArtistList;
            filteredTableView.ItemsSource = _filteredShowArtistList;

            fullArtistColumn.Binding = new System.Windows.Data.Binding("Artist.Name");
            fullVenueColumn.Binding = new System.Windows.Data.Binding("Show.Venue.Name");
            fullSoldSeatsColumn.Binding = new System.Windows.Data.Binding("Show.SoldSeats");
            fullAvailableSeatsColumn.Binding = new System.Windows.Data.Binding("Show.RemainingSeats");

            filteredArtistColumn.Binding = new System.Windows.Data.Binding("Artist.Name");
            filteredVenueColumn.Binding = new System.Windows.Data.Binding("Show.Venue.Name");
            filteredAvailableSeatsColumn.Binding = new System.Windows.Data.Binding("Show.RemainingSeats");

            fullDateColumn.Binding = new System.Windows.Data.Binding("Show.FormattedDate");
            filteredFromColumn.Binding = new System.Windows.Data.Binding("Show.FormattedTime");

            fullTableView.MouseDoubleClick += (s, e) =>
            {
                if (fullTableView.SelectedItem is ShowArtist sa)
                    _selectedShowArtist = sa;
            };

            filteredTableView.MouseDoubleClick += (s, e) =>
            {
                if (filteredTableView.SelectedItem is ShowArtist sa)
                    _selectedShowArtist = sa;
            };

            fullTableView.LoadingRow += (s, e) =>
            {
                if (e.Row.Item is ShowArtist sa && sa.Show.IsSoldOut())
                    e.Row.Background = System.Windows.Media.Brushes.LightCoral;
                else
                    e.Row.Background = System.Windows.Media.Brushes.Transparent;
            };

            filteredTableView.LoadingRow += (s, e) =>
            {
                if (e.Row.Item is ShowArtist sa && sa.Show.IsSoldOut())
                    e.Row.Background = System.Windows.Media.Brushes.LightCoral;
                else
                    e.Row.Background = System.Windows.Media.Brushes.Transparent;
            };
        }
        private void SetupKeyBindings()
        {
            buyerName.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter) numberOfSeats.Focus();
            };

            numberOfSeats.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter) SellTicketAction(s, e);
            };

            ticketId.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter) newNumberOfSeats.Focus();
            };

            newNumberOfSeats.KeyDown += (s, e) =>
            {
                if (e.Key == Key.Enter) ModifyTicketAction(s, e);
            };
        }
        private void Refresh()
        {
            _fullShowArtistList.Clear();
            foreach (var sa in _festivalService.FindAll())
                _fullShowArtistList.Add(sa);

            if (_selectedDate.HasValue)
            {
                _filteredShowArtistList.Clear();
                foreach (var sa in _festivalService.FindByDate(_selectedDate.Value))
                    _filteredShowArtistList.Add(sa);
            }
        }
        private void SellTicketAction(object sender, RoutedEventArgs e)
        {
            if (_selectedShowArtist == null)
            {
                ShowError("Please select a show first!");
                return;
            }
            try
            {
                int seats = int.Parse(numberOfSeats.Text);
                string buyer = buyerName.Text;
                _festivalService.SellTicket(_selectedShowArtist.Show, buyer, seats);
                Refresh();
                ShowInfo("Ticket has been successfully sold!");
            }
            catch (FormatException)
            {
                numberOfSeats.Clear();
                ShowError("Please enter a valid number!");
            }
            catch (Exception ex)
            {
                numberOfSeats.Clear();
                ShowError(ex.Message);
            }
        }
        private void ModifyTicketAction(object sender, RoutedEventArgs e)
        {
            try
            {
                int id = int.Parse(ticketId.Text);
                int seats = int.Parse(newNumberOfSeats.Text);
                _festivalService.ModifyTicket(id, seats);
                Refresh();
                ShowInfo("Ticket has been successfully modified!");
            }
            catch (FormatException)
            {
                ShowError("Please enter a valid number!");
            }
            catch (Exception ex)
            {
                ShowError(ex.Message);
            }
            finally
            {
                ticketId.Clear();
                newNumberOfSeats.Clear();
            }
        }
        private void SetFilterDate(object sender, RoutedEventArgs e)
        {
            _selectedDate = datePicker.SelectedDate;
            Refresh();
        }

        private void ShowError(string message) =>
            MessageBox.Show(message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);

        private void ShowInfo(string message) =>
            MessageBox.Show(message, "Info", MessageBoxButton.OK, MessageBoxImage.Information);

    }
}
