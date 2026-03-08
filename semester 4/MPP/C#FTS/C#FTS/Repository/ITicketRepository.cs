using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Repository
{
    internal interface ITicketRepository : IRepository<int, Ticket>
    {
        Ticket? IncrementSeats(Ticket ticket, int seats);
    }
}
