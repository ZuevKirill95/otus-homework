package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorMoveField11WithField12 implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field12 = message.getField12();

        Message.Builder result = message.toBuilder();

        result.field11(field12);
        result.field12(field11);

        return result.build();
    }
}
