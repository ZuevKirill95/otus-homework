package ru.otus.processor;

import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondException;

public class ProcessorThrowExceptionInEvenSecond implements Processor {
    private final EvenSecondService evenSecondService;

    public ProcessorThrowExceptionInEvenSecond(EvenSecondService evenSecondService) {
        this.evenSecondService = evenSecondService;
    }

    @Override
    public Message process(Message message) {
        if (evenSecondService.isEven()) {
            throw new EvenSecondException();
        }

        return message;
    }
}
