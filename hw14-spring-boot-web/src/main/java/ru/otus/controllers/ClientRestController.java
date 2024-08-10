package ru.otus.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.ClientRequest;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;

import java.util.List;

@RestController
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @PostMapping("/api/clients")
    void createClient(@RequestBody ClientRequest clientRequest) {
        Client client = extractClientFromRequest(clientRequest);
        dbServiceClient.saveClient(client);
    }

    private static Client extractClientFromRequest(ClientRequest clientRequest) {
        String name = clientRequest.name();
        String street = clientRequest.address();
        String number = clientRequest.phone();

        Address address = new Address(null, street);
        Phone phone = new Phone(null, number);

        return new Client(null, name, address, List.of(phone));
    }
}
