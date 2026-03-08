package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
public class Venue extends Entity<Integer> {
    private String name;
    private String address;
    private Integer capacity;

    public Venue(ResultSet rs) throws SQLException {
        super(rs.getInt("id"));
        this.name = rs.getString("name");
        this.address = rs.getString("address");
        this.capacity = rs.getInt("capacity");
    }
}
