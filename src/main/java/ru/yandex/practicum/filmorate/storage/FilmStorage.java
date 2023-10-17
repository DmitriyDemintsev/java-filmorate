package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film createFilm(Film film);

    Film putFilm(Film film);

    Map<Long, Film> getFilms();

    List<Film> findAll();

    Film getFilmById(long id);
}
