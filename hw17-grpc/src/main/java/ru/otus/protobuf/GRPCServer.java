package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.GenerateNumberServiceImpl;

import java.io.IOException;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var generateNumberService = new GenerateNumberServiceImpl();

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(generateNumberService).build();
        server.start();

        LOGGER.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
