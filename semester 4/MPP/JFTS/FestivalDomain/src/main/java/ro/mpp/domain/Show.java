package ro.mpp.domain;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Show extends Entity<Integer> {
    private final String date;
    private final String title;
    private Integer soldSeats;
    private final Venue venue;

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
                Timestamp.valueOf(LocalDateTime.now()),
                this
        );
    }

    public void addToSoldSeats(int numberOfSeats) {
        this.soldSeats += numberOfSeats;
    }
}
