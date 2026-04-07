package ro.mpp.net.protocol;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto.TicketDTO;
import ro.mpp.net.dto.UserDTO;

@Getter
@Builder
@ToString
public class Request {
    private RequestType requestType;

    private UserDTO user;           // for LOGIN/LOGOUT
    private String date;            // for FIND_BY_DATE
    private TicketDTO ticket;       // for BUY_TICKET
    private int TicketId;           // for MODIFY_TICKET
    private int seats;              // for MODIFY_TICKET
}
