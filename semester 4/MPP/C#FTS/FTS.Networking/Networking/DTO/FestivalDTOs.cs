
namespace C_FTS.Networking.DTO
{
    public class ArtsistDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public ArtsistDTO(int id, string name)
        {
            Id = id;
            Name = name;
        }
        public override string ToString()
        {
            return $"ArtistDTO(Id={Id}, Name={Name})";
        }
    }

    public class ShowDTO
    {
        public int Id { get; set; }
        public string Date { get; set; }
        public string Title { get; set; }
        public int SoldSeats { get; set; }
        public VenueDTO Venue { get; set; }
        public ShowDTO(int id, string date, string title, int soldSeats, VenueDTO venue)
        {
            Id = id;
            Date = date;
            Title = title;
            SoldSeats = soldSeats;
            Venue = venue;
        }
        public override string ToString()
        {
            return $"ShowDTO(Id={Id}, Date={Date}, Title={Title}, SoldSeats={SoldSeats}, Venue={Venue})";
        }
    }

    public class VenueDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public int Capacity { get; set; }
        public VenueDTO(int id, string name, string address, int capacity)
        {
            Id = id;
            Name = name;
            Address = address;
            Capacity = capacity;
        }
        public override string ToString()
        {
            return $"VenueDTO(Id={Id}, Name={Name}, Address={Address}, Capacity={Capacity})";
        }
    }

    public class TicketDTO {
        public int Id { get; set; }
        public string BuyerName { get; set; }
        public int NumberOfSeats { get; set; }
        public string PurchaseDate { get; set; }
        public ShowDTO Show { get; set; }
        public TicketDTO(int id = -1, string buyerName = "", int numberOfSeats = 0, string purchaseDate = "", ShowDTO show = null)
        {
            Id = id;
            BuyerName = buyerName;
            NumberOfSeats = numberOfSeats;
            PurchaseDate = purchaseDate;
            Show = show;
        }
        public override string ToString()
        {
            return $"TicketDTO(Id={Id}, BuyerName={BuyerName}, NumberOfSeats={NumberOfSeats}, PurchaseDate={PurchaseDate}, Show={Show})";
        }
    }

    public class UserDTO
    {
        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public UserDTO(int id, string username, string password)
        {
            Id = id;
            Username = username;
            Password = password;
        }
        public override string ToString()
        {
            return $"UserDTO(Id={Id}, Username={Username}, Password={Password})";
        }
    }

    public class ShowArtistDTO
    {
        public ShowDTO Show { get; set; }
        public ArtsistDTO Artist { get; set; }
        public ShowArtistDTO(ShowDTO show, ArtsistDTO artist)
        {
            Show = show;
            Artist = artist;
        }
        public override string ToString()
        {
            return $"ShowArtistDTO(Show={Show}, Artist={Artist})";
        }
    }
}
