package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
public class Ticket extends Entity<Integer> {
    private final String buyerName;
    @Setter
    private Integer numberOfSeats;
    private final Timestamp purchaseDate;
    private final Show show;

    public Ticket(ResultSet rs, Show show) throws SQLException {
        super(rs.getInt("id"));
        this.numberOfSeats = rs.getInt("number_of_seats");
        this.buyerName = rs.getString("buyer_name");
        this.purchaseDate = rs.getTimestamp("purchase_date");
        this.show = show;
    }

    public Ticket(int id, String buyerName, int numberOfSeats, Timestamp purchaseDate, Show show) {
        super(id);
        this.buyerName = buyerName;
        this.numberOfSeats = numberOfSeats;
        this.purchaseDate = purchaseDate;
        this.show = show;
    }

    public Ticket(String buyerName, int numberOfSeats, Timestamp purchaseDate, Show show) {
        super(-1);
        this.buyerName = buyerName;
        this.numberOfSeats = numberOfSeats;
        this.purchaseDate = purchaseDate;
        this.show = show;
    }
}
