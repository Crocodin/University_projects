using C_FTS.Domain;
using C_FTS.Repository.DBRepository;
using C_FTS.Service.Authenticator;
using FTS.Networking.Networking.Service;

using log4net;
using System.Collections.Concurrent;

namespace C_FTS.Service
{
    public class FestivalCSService : IFestivalSubject
    {
        private readonly ConcurrentDictionary<string, IFestivalObserver> _clients = new();

        private readonly ShowRepository _showRepository;
        private readonly TicketRepository _ticketRepository;
        private readonly ShowArtistRepository _showArtistRepository;
        private readonly IAuthenticator _authenticator;

        private static readonly ILog logger = LogManager.GetLogger(typeof(FestivalCSService));

        public FestivalCSService(ShowRepository showRepository, TicketRepository ticketRepository, ShowArtistRepository showArtistRepository, IAuthenticator authenticator)
        {
            _showRepository = showRepository;
            _ticketRepository = ticketRepository;
            _showArtistRepository = showArtistRepository;
            _authenticator = authenticator;
        }

        public List<ShowArtist> FindByDate(DateTime date)
        {
            lock (this)
            {
                return _showArtistRepository.FilterByDate(date);
            }
        }

        public List<ShowArtist> FindAll()
        {
            lock (this)
            {
                return _showArtistRepository.FindAll();
            }
        }

        public Ticket? SellTicket(Show show, string buyerName, int seats)
        {
            lock (this)
            {
                if (show.RemainingSeats >= seats)
                {
                    Ticket ticket = show.SellTicket(buyerName, seats);
                    Ticket? saved = _ticketRepository.Save(ticket);
                    if (saved != null)
                    {
                        show.AddToSoldSeats(seats);
                        _showRepository.Update(show);
                        NotifyAll(saved, isSold: true);
                    }
                    return saved;
                }
                throw new TicketModifierException("Not enough seats for the ticket");
            }
        }

        public bool ModifyTicket(int ticketId, int seats)
        {
            lock (this)
            {
                Ticket? ticket = _ticketRepository.Find(ticketId);
                if (ticket == null) return false;

                if (seats < 0) throw new TicketModifierException("Seats can't be negative");

                int seatDelta = seats - ticket.NumberOfSeats;
                Show show = ticket.Show;

                if (show.RemainingSeats - seatDelta >= 0)
                {
                    ticket.NumberOfSeats = seats;
                    Ticket? updated = _ticketRepository.IncrementSeats(ticket, seats);
                    if (updated != null)
                    {
                        updated.Show.AddToSoldSeats(seatDelta); // can be +/-
                        _showRepository.Update(updated.Show);
                    }
                }

                NotifyAll(ticket, isSold: false);
                return true;
            }
        }

        public void Login(string username, string password, IFestivalObserver observer)
        {
            lock (this)
            {
                _clients[username] = observer;
            }
        }

        public void Logout(string username)
        {
            lock (this)
            {
                _clients.TryRemove(username, out _);
            }
        }

        public User? Authenticate(string username, string password)
        {
            lock (this)
            {
                return _authenticator.Authenticate(username, password);
            }
        }

        // no-op on server side, only meaningful on client
        public void SetObserver(IFestivalObserver observer) { }

        private void NotifyAll(Ticket ticket, bool isSold)
        {
            logger.InfoFormat("Notifying {0} clients about ticket {1}", _clients.Count, ticket);
            foreach (var observer in _clients.Values)
            {
                if (isSold) observer.TicketSold(ticket);
                else observer.TicketModified(ticket);
            }
        }
    }
}