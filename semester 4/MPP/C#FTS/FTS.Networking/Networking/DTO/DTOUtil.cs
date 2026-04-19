using C_FTS.Domain;

namespace C_FTS.Networking.DTO
{
    public static class DTOUtil
    {
        public static UserDTO GetDTO(User user)
        {
            return new UserDTO(user.Id, user.Username, user.Password);
        }

        public static User GetFromDTO(UserDTO dto)
        {
            return new User(dto.Id, dto.Username, dto.Password);
        }

        public static VenueDTO GetDTO(Venue venue)
        {
            return new VenueDTO(venue.Id, venue.Name, venue.Address, venue.Capacity);
        }

        public static Venue GetFromDTO(VenueDTO dto)
        {
            return new Venue(dto.Id, dto.Name, dto.Address, dto.Capacity);
        }

        public static ShowDTO GetDTO(Show show)
        {
            return new ShowDTO(show.Id, show.Date, show.Title, show.SoldSeats, GetDTO(show.Venue));
        }

        public static Show GetFromDTO(ShowDTO dto)
        {
            return new Show(dto.Id, dto.Date, dto.Title, dto.SoldSeats, GetFromDTO(dto.Venue));
        }

        public static TicketDTO GetDTO(Ticket ticket)
        {
            return new TicketDTO(ticket.Id, ticket.BuyerName, ticket.NumberOfSeats, ticket.PurchaseDate, GetDTO(ticket.Show));
        }

        public static Ticket GetFromDTO(TicketDTO dto)
        {
            return new Ticket(dto.Id, dto.BuyerName, dto.NumberOfSeats, dto.PurchaseDate, GetFromDTO(dto.Show));
        }

        public static ArtsistDTO GetDTO(Artist artist)
        {
            return new ArtsistDTO(artist.Id, artist.Name);
        }

        public static Artist GetFromDTO(ArtsistDTO dto)
        {
            return new Artist(dto.Id, dto.Name);
        }

        public static ShowArtistDTO GetDTO(ShowArtist showArtist)
        {
            return new ShowArtistDTO(GetDTO(showArtist.Show), GetDTO(showArtist.Artist));
        }

        public static ShowArtist GetFromDTO(ShowArtistDTO dto)
        {
            return new ShowArtist(GetFromDTO(dto.Show), GetFromDTO(dto.Artist));
        }
    }
}
