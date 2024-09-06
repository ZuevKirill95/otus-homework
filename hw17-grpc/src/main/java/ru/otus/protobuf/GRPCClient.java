package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var generateNumberServiceBlockingStub = GenerateNumberServiceGrpc.newStub(channel);
        final Deque<Long> stackServerValues = new LinkedList<>();

        var latch = new CountDownLatch(1);
        generateNumberServiceBlockingStub.generate(GenerateNumberRequest.newBuilder()
                        .setFirstValue(0)
                        .setLastValue(30)
                        .build(),
                new StreamObserver<>() {

                    @Override
                    public void onNext(GenerateNumberResponse generateNumberResponse) {
                        long valueFromServer = generateNumberResponse.getGeneratedValue();
                        stackServerValues.addFirst(valueFromServer);
                        System.out.printf("new generated value on server: %s%n", valueFromServer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println(throwable);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("server finished generating values");
                        latch.countDown();
                    }
                });

        System.out.println("numbers Client is starting...");
        long prevValue = 0;
        long currentValue = 0;

        for (int i = 0; i < 50; i++) {
            Thread.sleep(1_000);
            Long valueFromServer = stackServerValues.pollFirst();
            if (valueFromServer == null || prevValue == valueFromServer) {
                currentValue = currentValue + 1;
            } else {
                currentValue = currentValue + valueFromServer + 1;
                prevValue = valueFromServer;
            }
            System.out.printf("currentValue: %s%n", currentValue);
        }

        System.out.println("request completed");

        latch.await();
        channel.shutdown();
    }
}
