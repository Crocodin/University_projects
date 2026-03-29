using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Service
{
    public class TicketModifier : ApplicationException
    {
        public TicketModifier() { }
        public TicketModifier(string message) : base(message) { }
        public TicketModifier(string message, Exception innerException) : base(message, innerException) { }
    }

    public interface IFestivalService
    {
        // ---------- SHOW-ARTIST
        public List<ShowArtist> FindByDate(DateTime date);
        public List<ShowArtist> FindAll();

        // ---------- TICKET
        public Ticket? SellTicket(Show show, string buyerName, int seats);
        public bool ModifyTicket(int ticketId, int seats);
    }
}
