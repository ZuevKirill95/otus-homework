package ru.otus.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    private Long id;

    private String street;

    public Address(String street) {
        this(null, street);
    }

    @PersistenceCreator
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
