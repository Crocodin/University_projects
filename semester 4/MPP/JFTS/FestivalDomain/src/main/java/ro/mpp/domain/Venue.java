package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class Venue extends Entity<Integer> {
    private final String name;
    private final String address;
    private final Integer capacity;

    public Venue(ResultSet rs) throws SQLException {
        super(rs.getInt("id"));
        this.name = rs.getString("name");
        this.address = rs.getString("address");
        this.capacity = rs.getInt("capacity");
    }

    public Venue(int id, String name, String address, Integer capacity) {
        super(id);
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }
}
