using C_FTS.Domain;
using C_FTS.Repository;
using C_FTS.Repository.DBRepository;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Service
{
    public class FestivalService : IFestivalService
    {
        private IShowRepository _showRepository = new ShowRepository();
        private ITicketRepository _ticketRepository = new TicketRepository();
        private IShowArtistRepository _showArtistRepository = new ShowArtistRepository();

        public List<ShowArtist> FindAll()
        {
            return _showArtistRepository.FindAll();
        }

        public List<ShowArtist> FindByDate(DateTime date)
        {
            return _showArtistRepository.FilterByDate(date);
        }

        public bool ModifyTicket(int ticketId, int seats)
        {
            var opT = _ticketRepository.Find(ticketId);
            if (opT == null) return false;

            if (seats < 0) throw new TicketModifier("Seat's can't be negativ");
            int newSeats = seats - opT.NumberOfSeats;
            Show show = opT.Show;
            if (show.RemainingSeats - newSeats >= 0)
            {
                opT.NumberOfSeats = seats;
                var newT = _ticketRepository.IncrementSeats(opT, seats);
                if (newT != null)
                {
                    newT.Show.AddToSoldSeats(newSeats);
                    _showRepository.Update(newT.Show);
                }
            }
            return true;
        }

        public Ticket? SellTicket(Show show, string buyerName, int seats)
        {
            Ticket ticket = show.SellTicket(buyerName, seats);
            if (show.RemainingSeats >= seats)
            {
                var t = _ticketRepository.Save(ticket);
                if (t != null)
                {
                    show.AddToSoldSeats(seats);
                    _showRepository.Update(show);
                }
                return t;
            }
            throw new TicketModifier("Not enough seats for the ticket");
        }
    }
}
