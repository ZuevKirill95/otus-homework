package ru.otus.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

@Table(name = "client")
@Getter
@ToString
public class Client {

    @Id
    @Column("id")
    private final Long id;

    @Column("name")
    private final String name;

    @MappedCollection(idColumn = "id")
    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
