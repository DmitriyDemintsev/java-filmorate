package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static LocalDate checkData = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage = new FilmStorage();

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        validateFilm(film);
        return filmStorage.put(film);
    }

    @GetMapping
    public ArrayList<Film> findAll() {
        return filmStorage.findAll();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.debug("Отсутствует название фильма");
            throw new ValidationException("Не указано название фильма");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.debug("Description выходит за 200 знаков");
            throw new ValidationException("Описание больше положенного, допустимо максимум 200 знаков, у вас "
                    + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(checkData)) {
            log.debug("Дата релиза старше 28 декабря 1895 года");
            throw new ValidationException("Проверьте дату релиза фильма, он не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.debug("Продолжительность фильма - отрицательное число");
            throw new ValidationException("Переданная продолжительность фильма – отрицательное число");
        }
    }
}
