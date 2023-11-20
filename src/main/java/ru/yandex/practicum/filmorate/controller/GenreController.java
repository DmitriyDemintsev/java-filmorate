package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.MovieGenre;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final FilmDbStorage filmDbStorage;

    public GenreController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    @GetMapping
    public List<MovieGenre> getGenresFromTable() {
        return filmDbStorage.getGenresFromTable();
    }

    @GetMapping("/{id}")
    public MovieGenre getGenreNameById(@PathVariable("id") Long id) {
        return filmDbStorage.getGenreNameById(id);
    }
}
