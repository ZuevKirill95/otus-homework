package ru.otus.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private Long id;

    private String street;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
