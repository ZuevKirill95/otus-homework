package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        Map<String, Object> paramsMap = Map.of("clients", clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
