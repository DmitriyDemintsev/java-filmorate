package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FilmStorage {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    protected int idGeneratorForFilm = 0;

    private int generateIdForFilm() {
        return ++idGeneratorForFilm;
    }

    public Film create(Film film) {
        film.setId(generateIdForFilm());
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Попытка обновить отсуствующий в Map<Integer, Film> films фильм");
            throw new ValidationException("Фильм, который вы хотите обновить, отсутствует в вашей коллекции");
        }
        return film;
    }

    public ArrayList<Film> findAll() {
        log.debug("Текущее количество фильмоф: {}", films.size());
        return new ArrayList<>(films.values());
    }
}
