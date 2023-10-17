package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    protected long idGeneratorForFilm = 0;

    private long generateIdForFilm() {
        return ++idGeneratorForFilm;
    }

    public Map<Long, Film> getFilms() {
        return new HashMap<>(films);
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(generateIdForFilm());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Попытка обновить отсуствующий в Map<Long, Film> films фильм");
            throw new FilmNotFoundException("Фильм, который вы хотите обновить, отсутствует в вашей коллекции");
        }
        return film;
    }

    public ArrayList<Film> findAll() {
        log.debug("Текущее количество фильмоф: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с таким id " + id + "не найден");
        }
        return films.get(id);
    }
}
