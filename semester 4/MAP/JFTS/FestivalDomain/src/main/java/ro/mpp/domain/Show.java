package ro.mpp.domain;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Show extends Entity<Integer> {
    private final Timestamp date;
    private final String title;
    private final Integer soldSeats;
    private final List<Artist> performers;
    private final Venue venue;

    public Show(Integer id, Timestamp date, String title, Integer soldSeats, List<Artist> performers, Venue venue) {
        super(id);
        this.date = date;
        this.title = title;
        this.soldSeats = soldSeats;
        this.performers = performers;
        this.venue = venue;
    }

    public Show(ResultSet rs, List<Artist> performers, Venue venue) throws SQLException {
        super(rs.getInt("id"));
        this.date = rs.getTimestamp("date");
        this.title = rs.getString("title");
        this.soldSeats = rs.getInt("soldSeats");
        this.performers = performers;
        this.venue = venue;
    }

    public boolean isSoldOut() {
        return this.venue.getCapacity() <= this.soldSeats;
    }

    public Integer remainingSeats() {
        return this.venue.getCapacity() - this.soldSeats;
    }

    // this function will always buy the ticket, it won't verify if there are seats
    public Ticket sellTicket(String buyerName, Integer numberOfSeats) {
        return new Ticket(
                buyerName,
                numberOfSeats,
                Timestamp.valueOf(LocalDateTime.now()),
                this
        );
    }
}
