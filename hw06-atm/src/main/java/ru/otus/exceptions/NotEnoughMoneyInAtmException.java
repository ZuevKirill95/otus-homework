package ru.otus.exceptions;

public class NotEnoughMoneyInAtmException extends RuntimeException {
    public NotEnoughMoneyInAtmException(String message) {
        super(message);
    }

    public NotEnoughMoneyInAtmException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughMoneyInAtmException() {
        super("Not enough money in atm.");
    }
}
