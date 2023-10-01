package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    static final int MAX_LENGTH_DESCRIPTION = 200;
    LocalDate checkData = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    protected int idGeneratorForFilm = 0;

    private int generateIdForFilm() {
        return ++idGeneratorForFilm;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        film.setId(generateIdForFilm());
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Попытка обновить отсуствующий в Map<Integer, Film> films фильм");
            throw new ValidationException("Фильм, который вы хотите обновить, отсутствует в вашей коллекции");
        }
        return film;
    }

    @GetMapping
    public ArrayList<Film> findAll() {
        log.debug("Текущее количество фильмоф: {}", films.size());
        return new ArrayList<>(films.values());
    }

    protected void validateFilm (@RequestBody Film film) {
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
