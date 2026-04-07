package ro.mpp.net.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto._netutils.FestivalDTO;

@Getter
@AllArgsConstructor
@ToString
public class VenueDTO implements FestivalDTO {
    private int id;
    private final String name;
    private final String address;
    private final Integer capacity;
}
