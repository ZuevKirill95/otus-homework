package ru.otus.exceptions;

public class NotNeededNominalInAtmException extends RuntimeException {
    public NotNeededNominalInAtmException(String message) {
        super(message);
    }

    public NotNeededNominalInAtmException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotNeededNominalInAtmException() {
        super("Not needed nominal in atm");
    }
}
