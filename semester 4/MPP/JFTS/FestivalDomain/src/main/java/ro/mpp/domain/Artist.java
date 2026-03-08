package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
@ToString
public class Artist extends Entity<Integer> {
    private final String name;

    public Artist(ResultSet rs) throws SQLException {
        super(rs.getInt("id"));
        this.name = rs.getString("name");
    }
}

