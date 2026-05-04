package ro.mpp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "show")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show extends HasId<Integer> {
    @Column(name = "date")
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "sold_seats")
    private Integer soldSeats;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public Show(Integer id, String date, String title, Integer soldSeats, Venue venue) {
        super(id);
        this.date = date;
        this.title = title;
        this.soldSeats = soldSeats;
        this.venue = venue;
    }

    public Show(ResultSet rs, Venue venue) throws SQLException {
        super(rs.getInt("id"));
        this.date = rs.getString("date");
        this.title = rs.getString("title");
        this.soldSeats = rs.getInt("sold_seats");
        this.venue = venue;
    }

    public boolean isSoldOut() {
        return this.venue.getCapacity() <= this.soldSeats;
    }

    public Integer remainingSeats() {
        return this.venue.getCapacity() - this.soldSeats;
    }

    public Ticket sellTicket(String buyerName, Integer numberOfSeats) {
        return new Ticket(
                buyerName,
                numberOfSeats,
                LocalDateTime.now().toString(),
                this
        );
    }

    public void addToSoldSeats(int numberOfSeats) {
        this.soldSeats += numberOfSeats;
    }
}
