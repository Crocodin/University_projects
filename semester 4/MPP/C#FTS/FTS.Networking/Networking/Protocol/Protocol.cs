using C_FTS.Networking.DTO;

namespace C_FTS.Networking.Protocol
{
    public enum ResponseType
    {
        OK,
        ERROR,
        TICKET_SOLD,
        TICKET_MODIFIED,
        FIND_BY_DATE,
        FIND_ALL
    }
    public class Response
    {
        public ResponseType Type { get; }
        public string Message { get; } = string.Empty;
        // ------------------------------------
        public UserDTO? User { get; } = null;
        public ShowArtistDTO[] ShowArtist { get; } = Array.Empty<ShowArtistDTO>();
        public TicketDTO? Ticket { get; } = null;

        public Response(ResponseType type, string message = "", UserDTO? user = null, ShowArtistDTO[] showArtist = null, TicketDTO? ticket = null)
        {
            Type = type;
            Message = message;
            User = user;
            ShowArtist = showArtist ?? Array.Empty<ShowArtistDTO>();
            Ticket = ticket;
        }
    }
    public enum RequestType
    {
        LOGIN,
        LOGOUT,
        BUY_TICKET,
        MODIFY_TICKET,
        FIND_BY_DATE,
        FIND_ALL
    }
    public class Request
    {
        public RequestType Type { get; }
        public string Message { get; } = string.Empty;
        // ------------------------------------
        public UserDTO? User { get; } = null;
        public ShowArtistDTO? ShowArtist { get; } = null;
        public TicketDTO? Ticket { get; } = null;
        public int Seats { get; } = 0;
        public string Date { get; } = string.Empty;
        public int TicketId { get; } = 0;
        public Request(RequestType type, string message = "", UserDTO? user = null, ShowArtistDTO? showArtist = null, TicketDTO? ticket = null, int seats = 0, string date = "", int ticketId = 0)
        {
            Type = type;
            Message = message;
            User = user;
            ShowArtist = showArtist;
            Ticket = ticket;
            Seats = seats;
            Date = date;
            TicketId = ticketId;
        }
    }
}
