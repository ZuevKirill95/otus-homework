package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GRPCServer.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var generateNumberServiceBlockingStub = GenerateNumberServiceGrpc.newStub(channel);
        final AtomicLong serverValue = new AtomicLong(0);

        var latch = new CountDownLatch(1);
        generateNumberServiceBlockingStub.generate(GenerateNumberRequest.newBuilder()
                        .setFirstValue(0)
                        .setLastValue(30)
                        .build(),
                new StreamObserver<>() {

                    @Override
                    public void onNext(GenerateNumberResponse generateNumberResponse) {
                        long valueFromServer = generateNumberResponse.getGeneratedValue();
                        serverValue.set(valueFromServer);
                        LOGGER.info("new generated value on server: {}", valueFromServer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println(throwable);
                    }

                    @Override
                    public void onCompleted() {
                        LOGGER.info("server finished generating values");
                        latch.countDown();
                    }
                });

        LOGGER.info("numbers Client is starting...");
        long prevValue = 0;
        long currentValue = 0;

        for (int i = 0; i < 50; i++) {
            Thread.sleep(1_000);
            Long valueFromServer = serverValue.get();
            if (prevValue == valueFromServer) {
                currentValue = currentValue + 1;
            } else {
                currentValue = currentValue + valueFromServer + 1;
                prevValue = valueFromServer;
            }
            LOGGER.info("currentValue: {}", currentValue);
        }

        LOGGER.info("request completed");

        latch.await();
        channel.shutdown();
    }
}
