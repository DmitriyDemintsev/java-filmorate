package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {

    public MPAController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    private final FilmDbStorage filmDbStorage;

    @GetMapping
    public List<MPA> getMpaFromTable() {

        return filmDbStorage.getMpaFromTable();
    }

    @GetMapping("/{id}")
    public MPA getMpaRatingById(@PathVariable("id") Long id) {

        return filmDbStorage.getMpaRatingById(id);
    }
}
