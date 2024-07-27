package ru.otus.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientRequest;

import java.io.IOException;
import java.util.List;

@SuppressWarnings({"squid:S1948"})
public class ClientsApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = extractClientFromRequest(request);
        Client createdClient = dbServiceClient.saveClient(client);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(createdClient.getId()));
    }

    private static Client extractClientFromRequest(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClientRequest clientRequest = mapper.readValue(request.getInputStream(), ClientRequest.class);

        String name = clientRequest.name();
        String street = clientRequest.address();
        String number =  clientRequest.phone();

        Address address = new Address(null, street);
        Phone phone = new Phone(null, number);

        return new Client(null, name, address, List.of(phone));
    }
}
