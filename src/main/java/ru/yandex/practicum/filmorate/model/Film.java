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
    private Set<MovieGenre> genres;
    private MPA mpa;

    public Film() {
        this.genres = new HashSet<>();
        this.likes = new HashSet<>();
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = new HashSet<>();
        this.likes = new HashSet<>();
        this.mpa = mpa;
    }


    public void addLike(long id) {
        likes.add(id);
    }

    public void removeLike(long id) {
        likes.remove(id);
    }

    public void addGenre(MovieGenre genre) {
        genres.add(genre);
    }
}
