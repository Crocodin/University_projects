using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace FTS.Networking.Networking.Service
{
    public interface IFestivalObserver
    {
        void TicketSold(Ticket ticket);
        void TicketModified(Ticket ticket);
    }
}
