package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Ticket extends Entity<Integer> {
    private final String buyerName;
    @Setter
    private Integer numberOfSeats;
    private final Timestamp purchaseDate;
    private final Show show;

    public Ticket(ResultSet rs, Show show) throws SQLException {
        super(show.getId());
        this.numberOfSeats = rs.getInt("numberOfSeats");
        this.buyerName = rs.getString("buyerName");
        this.purchaseDate = rs.getTimestamp("purchaseDate");
        this.show = show;
    }
}
