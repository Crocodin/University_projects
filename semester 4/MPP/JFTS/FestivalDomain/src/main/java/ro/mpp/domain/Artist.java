package ro.mpp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@ToString
@Entity
@Table(name = "artist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends HasId<Integer> {
    @Column(name = "name")
    private String name;

    public Artist(ResultSet rs) throws SQLException {
        super(rs.getInt("id"));
        this.name = rs.getString("name");
    }

    public Artist(int id, String name) {
        super(id);
        this.name = name;
    }
}

