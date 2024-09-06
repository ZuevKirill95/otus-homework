package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.GenerateNumberServiceImpl;

import java.io.IOException;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var generateNumberService = new GenerateNumberServiceImpl();

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(generateNumberService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
