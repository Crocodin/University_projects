package ro.mpp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Entity<ID> {
    private ID id;

    public Entity(ID id) {
        this.id = id;
    }
}
