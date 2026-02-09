package org.example.template.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Entity<ID> {
    private ID id;
}
