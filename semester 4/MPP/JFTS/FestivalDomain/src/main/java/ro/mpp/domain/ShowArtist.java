package ro.mpp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShowArtist extends Entity<Integer> {
    Show show;
    Artist artist;

    public ShowArtist(Show show, Artist artist) {
        super(null);
        this.show = show;
        this.artist = artist;
    }
}
