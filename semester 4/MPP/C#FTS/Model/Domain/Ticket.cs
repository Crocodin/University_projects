using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Domain
{
    public class Ticket : Entity<int>
    {
        public string BuyerName { get; }
        public int NumberOfSeats { get; set; }
        public string PurchaseDate { get; }
        public Show Show { get; }

        public Ticket(int id, string buyerName, int numberOfSeats, string purchaseDate, Show show) : base(id)
        {
            BuyerName = buyerName;
            NumberOfSeats = numberOfSeats;
            PurchaseDate = purchaseDate;
            Show = show;
        }

        public Ticket(string buyerName, int numberOfSeats, string purchaseDate, Show show) : base(-1)
        {
            BuyerName = buyerName;
            NumberOfSeats = numberOfSeats;
            PurchaseDate = purchaseDate;
            Show = show;
        }

        public Ticket(IDataReader reader, Show show)
            : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            BuyerName = reader.GetString(reader.GetOrdinal("buyer_name"));
            NumberOfSeats = reader.GetInt32(reader.GetOrdinal("number_of_seats"));
            PurchaseDate = reader.GetString(reader.GetOrdinal("purchase_date"));
            Show = show;
        }

        public override string ToString()
        {
            return $"Ticket(id={Id}, BuyerName={BuyerName}, PurchesDate={PurchaseDate}, Show={Show})";
        }
    }
}
