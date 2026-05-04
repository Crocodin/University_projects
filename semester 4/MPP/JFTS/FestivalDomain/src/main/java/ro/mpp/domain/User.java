package ro.mpp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User extends HasId<Integer> {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

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
