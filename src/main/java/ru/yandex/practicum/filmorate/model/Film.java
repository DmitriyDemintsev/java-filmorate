package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.*;

@Data

public class Film {

    private Long id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Long> likes;
    private Set<FilmGenre> filmGenre; // надо передавать в конструктор
    private FilmRating filmRating; // надо передавать в конструктор

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.filmGenre = new HashSet<>();
        this.filmRating = filmRating;
        this.likes = new HashSet<>();
    }

    public void addLike(long id) {
        likes.add(id);
    }

    public void removeLike(long id) {
        likes.remove(id);
    }

    public Set<FilmGenre> getFilmGenre() {
        return filmGenre;
    }

    public FilmRating getFilmRating() {
        return filmRating;
    }
}
