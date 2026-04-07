package ro.mpp.domain;

import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@ToString
public class User extends Entity<Integer> {
    private final String username;
    private final String password;

    public User(ResultSet rs) throws SQLException {
        super(rs.getInt("id"));
        this.username = rs.getString("username");
        this.password = rs.getString("password");
    }

    public User(int id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }
}
