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
    private Set<FilmGenre> genres;
    private MPA rating;

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = new HashSet<>();
        this.rating = rating;
        this.likes = new HashSet<>();
    }

    public void addLike(long id) {
        likes.add(id);
    }

    public void removeLike(long id) {
        likes.remove(id);
    }

    public Set<FilmGenre> getGenre() {
        return genres;
    }

    public MPA getFilmRating() {
        return rating;
    }
}
