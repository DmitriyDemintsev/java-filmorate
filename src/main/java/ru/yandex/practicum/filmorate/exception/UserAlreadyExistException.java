package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
