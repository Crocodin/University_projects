package ro.mpp.net.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto._netutils.FestivalDTO;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class TicketDTO implements FestivalDTO {
    private int id;
    private String buyerName;
    private int seats;
    private String date;
    private ShowDTO show;
}
