package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(final String message) {
        super(message);
    }
}
