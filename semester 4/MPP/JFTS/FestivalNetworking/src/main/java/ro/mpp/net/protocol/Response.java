package ro.mpp.net.protocol;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto.ShowArtistDTO;
import ro.mpp.net.dto.TicketDTO;
import ro.mpp.net.dto.UserDTO;

@Getter
@Builder
@ToString
public class Response {
    private ResponseType responseType;
    private String errorMessage;

    private UserDTO user;
    private ShowArtistDTO[] showArtists;
    private TicketDTO ticket;
}
