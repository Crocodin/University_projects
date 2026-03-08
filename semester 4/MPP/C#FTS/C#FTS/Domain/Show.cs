using System;
using System.Data;

namespace C_FTS.Domain
{
    public class Show : Entity<int>
    {
        public DateTime Date { get; }
        public string Title { get; }
        public int SoldSeats { get; private set; }
        public List<Artist> Performers { get; }
        public Venue Venue { get; }

        public Show(int id, DateTime date, string title, int soldSeats, List<Artist> performers, Venue venue)
            : base(id)
        {
            Date = date;
            Title = title ?? throw new ArgumentNullException(nameof(title));
            SoldSeats = soldSeats;
            Performers = performers ?? new List<Artist>();
            Venue = venue ?? throw new ArgumentNullException(nameof(venue));
        }

        public Show(IDataReader reader, List<Artist> performers, Venue venue)
            : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            Date = reader.GetDateTime(reader.GetOrdinal("date"));
            Title = reader.GetString(reader.GetOrdinal("title"));
            SoldSeats = reader.GetInt32(reader.GetOrdinal("soldSeats"));
            Performers = performers ?? new List<Artist>();
            Venue = venue ?? throw new ArgumentNullException(nameof(venue));
        }

        public bool IsSoldOut()
        {
            return Venue.Capacity <= SoldSeats;
        }

        public int RemainingSeats()
        {
            return Venue.Capacity - SoldSeats;
        }

        // This function always sells the ticket without checking capacity
        public Ticket SellTicket(string buyerName, int numberOfSeats)
        {
            return new Ticket(
                buyerName,
                numberOfSeats,
                DateTime.Now,
                this
            );
        }
    }
}
