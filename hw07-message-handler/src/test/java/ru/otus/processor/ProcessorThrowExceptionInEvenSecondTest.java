package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessorThrowExceptionInEvenSecondTest {
    @Test
    @DisplayName("Вызов метода не должен выбрасывать исключения, если секунда нечетная")
    void methodCallShouldNotThrowExceptionsIfTheSecondIsOdd() {
        // given
        var field11 = "field11";
        var message = new Message.Builder(1L)
                .field11(field11)
                .build();

        EvenSecondService evenSecondService = mock(EvenSecondService.class);
        when(evenSecondService.isEven()).thenReturn(false);
        var processorThrowExceptionInEvenSecond = new ProcessorThrowExceptionInEvenSecond(evenSecondService);

        // when
        Message process = processorThrowExceptionInEvenSecond.process(message);

        // then
        assertEquals(field11, process.getField11());
    }

    @Test
    @DisplayName("Вызов метода должен выбрасывать исключения, если секунда четная")
    void methodCallShouldThrowExceptionsIfTheSecondIsEven() {
        // given
        var field11 = "field11";
        var message = new Message.Builder(1L)
                .field11(field11)
                .build();

        EvenSecondService evenSecondService = mock(EvenSecondService.class);
        when(evenSecondService.isEven()).thenReturn(true);
        var processorThrowExceptionInEvenSecond = new ProcessorThrowExceptionInEvenSecond(evenSecondService);

        // when
        assertThrows(EvenSecondException.class,
                () -> processorThrowExceptionInEvenSecond.process(message));
    }
}