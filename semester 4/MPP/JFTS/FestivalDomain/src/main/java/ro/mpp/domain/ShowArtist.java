package ro.mpp.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "show_artist")
public class ShowArtist {
    @EmbeddedId
    private ShowArtistId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("showId")
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public ShowArtist(Show show, Artist artist) {
        this.show = show;
        this.artist = artist;
        this.id = new ShowArtistId(show.getId(), artist.getId());
    }
}
