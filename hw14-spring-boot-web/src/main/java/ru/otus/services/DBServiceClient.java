package ru.otus.services;

import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    List<Client> findAll();
}
