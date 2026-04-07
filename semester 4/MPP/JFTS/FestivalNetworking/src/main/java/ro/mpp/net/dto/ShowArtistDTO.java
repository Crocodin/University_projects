package ro.mpp.net.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto._netutils.FestivalDTO;

@Getter
@AllArgsConstructor
@ToString
public class ShowArtistDTO implements FestivalDTO {
    private ShowDTO show;
    private ArtistDTO artist;
}
