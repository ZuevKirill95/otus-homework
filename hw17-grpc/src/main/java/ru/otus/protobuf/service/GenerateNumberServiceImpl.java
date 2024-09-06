package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.GenerateNumberRequest;
import ru.otus.protobuf.GenerateNumberResponse;
import ru.otus.protobuf.GenerateNumberServiceGrpc;

public class GenerateNumberServiceImpl extends GenerateNumberServiceGrpc.GenerateNumberServiceImplBase {
    @Override
    public void generate(GenerateNumberRequest request, StreamObserver<GenerateNumberResponse> responseObserver) {
        for (long i = request.getFirstValue() + 1; i <= request.getLastValue(); i++) {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            responseObserver.onNext(GenerateNumberResponse.newBuilder().setGeneratedValue(i).build());
        }

        responseObserver.onCompleted();
    }
}
