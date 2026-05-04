package ro.mpp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "ticket")
public class Ticket extends HasId<Integer> {
    @Column(name = "buyer_name")
    private String buyerName;

    @Setter
    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    private Show show;

    public Ticket(ResultSet rs, Show show) throws SQLException {
        super(rs.getInt("id"));
        this.numberOfSeats = rs.getInt("number_of_seats");
        this.buyerName = rs.getString("buyer_name");
        this.purchaseDate = rs.getString("purchase_date");
        this.show = show;
    }

    public Ticket(int id, String buyerName, int numberOfSeats, String purchaseDate, Show show) {
        super(id);
        this.buyerName = buyerName;
        this.numberOfSeats = numberOfSeats;
        this.purchaseDate = purchaseDate;
        this.show = show;
    }

    public Ticket(String buyerName, int numberOfSeats, String purchaseDate, Show show) {
        super(-1);
        this.buyerName = buyerName;
        this.numberOfSeats = numberOfSeats;
        this.purchaseDate = purchaseDate;
        this.show = show;
    }
}
