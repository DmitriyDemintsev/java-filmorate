package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handlerValidationException(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handlerUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handlerFilmNotFoundException(final FilmNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handlerMPANotFoundException(final MPANotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handlerGenreNotFoundException(final GenreNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //ошибка 500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка", e);
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }
}
