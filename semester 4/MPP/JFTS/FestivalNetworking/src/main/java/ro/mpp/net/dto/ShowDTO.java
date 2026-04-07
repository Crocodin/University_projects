package ro.mpp.net.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto._netutils.FestivalDTO;

@Getter
@AllArgsConstructor
@ToString
public class ShowDTO implements FestivalDTO {
    private int id;
    private String title;
    private String date;
    private int soldSeats;
    private VenueDTO venueDTO;
}
