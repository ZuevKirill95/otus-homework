package ru.otus.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "phone")
public class Phone {
    @Id
    private final Long id;

    private final Long clientId;

    private final String number;

    @PersistenceCreator
    public Phone(Long id, Long clientId, String number) {
        this.id = id;
        this.clientId = clientId;
        this.number = number;
    }

    public Phone(String number) {
        this(null, null, number);
    }
}
