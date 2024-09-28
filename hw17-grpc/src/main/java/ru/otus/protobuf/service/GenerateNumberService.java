package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.GenerateNumberResponse;

public interface GenerateNumberService {
    void generateValue(long firstValue, long lastValue, StreamObserver<GenerateNumberResponse> responseObserver);
}
