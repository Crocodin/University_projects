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
        this.numberOfSeats = rs.getInt("number_of_seats");
        this.buyerName = rs.getString("buyer_same");
        this.purchaseDate = rs.getTimestamp("purchase_date");
        this.show = show;
    }
}
