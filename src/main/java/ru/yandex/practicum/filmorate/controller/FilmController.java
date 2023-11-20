package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private final FilmService filmService;
    private final UserService userService;
    private final FilmDbStorage filmDbStorage;
    private static LocalDate checkData = LocalDate.of(1895, 12, 28);

    public FilmController(FilmService filmService, UserService userService, FilmDbStorage filmDbStorage) {
        this.filmService = filmService;
        this.userService = userService;
        this.filmDbStorage = filmDbStorage;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        filmService.getFilmById(film.getId());
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Long userId) {
        filmService.addLike(filmService.getFilmById(id), userService.getUserById(userId));
    }

    @GetMapping
    public List<Film> findFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dellLike(@PathVariable("id") Integer id, @PathVariable("userId") Long userId) {
        filmService.dellLike(filmService.getFilmById(id), userService.getUserById(userId));
    }

    @GetMapping("/popular")
    public List<Film> getFilmByLikes(@RequestParam(value = "count", required = false) Integer count) {
        return filmService.getFilmByLikes(Objects.requireNonNullElse(count, 10));
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
