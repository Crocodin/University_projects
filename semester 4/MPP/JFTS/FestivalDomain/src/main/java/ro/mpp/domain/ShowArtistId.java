package ro.mpp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShowArtistId implements Serializable {
    @Column(name = "show_id")
    private Integer showId;

    @Column(name = "artist_id")
    private Integer artistId;
}
