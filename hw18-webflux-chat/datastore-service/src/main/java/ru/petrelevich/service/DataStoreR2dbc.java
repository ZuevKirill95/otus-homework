package ru.petrelevich.service;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.petrelevich.domain.Message;
import ru.petrelevich.repository.MessageRepository;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private static final String ROOM_1408 = "1408";
    private final MessageRepository messageRepository;
    private final Scheduler workerPool;

    public DataStoreR2dbc(Scheduler workerPool, MessageRepository messageRepository) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        if (message.getRoomId().equals(ROOM_1408)) {
            throw new IllegalArgumentException(
                    "В комнату %s нельзя писать сообщения".formatted(ROOM_1408));
        }

        log.info("saveMessage:{}", message);
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);

        if (roomId.equals(ROOM_1408)) {
            return messageRepository.findAll().delayElements(Duration.ofSeconds(1), workerPool);
        }

        return messageRepository.findByRoomId(roomId).delayElements(Duration.of(3, SECONDS), workerPool);
    }
}
