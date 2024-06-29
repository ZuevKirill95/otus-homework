package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessorMoveField11WithField12Test {
    @Test
    @DisplayName("Тестируем перемещение полей field11 и field12")
    void notifyTest() {
        // given
        var field11 = "field11";
        var field12 = "filed12";

        var message = new Message.Builder(1L)
                .field11(field11)
                .field12(field12)
                .build();

        var processorMoveField11WithField12 = new ProcessorMoveField11WithField12();

        // when
        Message process = processorMoveField11WithField12.process(message);

        // then
        assertEquals(field12, process.getField11());
        assertEquals(field11, process.getField12());
    }
}