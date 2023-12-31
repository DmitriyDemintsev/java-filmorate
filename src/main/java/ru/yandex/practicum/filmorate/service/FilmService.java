package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage")FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Film film, User user) { // добвляем лайки фильму
        if (!film.getLikes().contains(user.getId())) {
            filmStorage.addLike(film, user);
        } else {
            System.out.println("Вы уже отметили этот фильм как понравившийся");
        }
    }

    public void dellLike(Film film, User user) { // удаляем лайки
        if (film.getLikes().contains(user.getId())) {
            filmStorage.dellLike(film, user);
        } else {
            System.out.println("Вы не отмечали этот фильм как понравившийся");
        }
    }

    public List<Film> getFilmByLikes(int countFilm) {
        List<Film> filmsSorted = new ArrayList<>();
        MovieLikesComparator movieLikesComparator = new MovieLikesComparator();
        for (Film film: filmStorage.findFilms()) {
            filmsSorted.add(film);
        }
        filmsSorted.sort(movieLikesComparator);
        return filmsSorted.subList(0, Math.min(countFilm, filmsSorted.size()));
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}
