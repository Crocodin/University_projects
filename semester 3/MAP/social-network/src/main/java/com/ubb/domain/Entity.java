package com.ubb.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity<ID> {
    private ID id;

    public Entity(ID id) { this.id = id; };
}
