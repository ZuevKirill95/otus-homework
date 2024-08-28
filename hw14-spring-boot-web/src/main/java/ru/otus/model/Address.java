package ru.otus.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    @Column("client_id")
    private Long addressId;

    private String street;

    public Address(String street) {
        this(null, street);
    }

    @PersistenceCreator
    public Address(Long addressId, String street) {
        this.addressId = addressId;
        this.street = street;
    }
}
