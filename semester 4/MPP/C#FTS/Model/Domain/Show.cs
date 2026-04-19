using System;
using System.Data;

namespace C_FTS.Domain
{
    public class Show : Entity<int>
    {
        public string Date { get; set; }
        public string Title { get; }
        public int SoldSeats { get; private set; }
        public Venue Venue { get; }

        public Show(int id, string date, string title, int soldSeats, Venue venue)
            : base(id)
        {
            Date = date;
            Title = title ?? throw new ArgumentNullException(nameof(title));
            SoldSeats = soldSeats;
            Venue = venue ?? throw new ArgumentNullException(nameof(venue));
        }

        public Show(IDataReader reader, Venue venue)
            : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            Date = reader.GetString(reader.GetOrdinal("date"));
            Title = reader.GetString(reader.GetOrdinal("title"));
            SoldSeats = reader.GetInt32(reader.GetOrdinal("sold_seats"));
            Venue = venue ?? throw new ArgumentNullException(nameof(venue));
        }

        public bool IsSoldOut()
        {
            return Venue.Capacity <= SoldSeats;
        }

        // This function always sells the ticket without checking capacity
        public Ticket SellTicket(string buyerName, int numberOfSeats)
        {
            return new Ticket(
                buyerName,
                numberOfSeats,
                DateTime.Now.ToString("yyyy-MM-ddTHH:mm:ss"),
                this
            );
        }

        public void AddToSoldSeats(int numberOfSoldSeats)
        {
            this.SoldSeats += numberOfSoldSeats;
        }

        public string FormattedDate => Date.Substring(0, 10);
        public string FormattedTime => Date.Substring(11, 5);
        public int RemainingSeats => Venue.Capacity - SoldSeats;
    }
}
