package ro.mpp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "venue")
public class Venue extends HasId<Integer> {
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "capacity")
    private Integer capacity;

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
