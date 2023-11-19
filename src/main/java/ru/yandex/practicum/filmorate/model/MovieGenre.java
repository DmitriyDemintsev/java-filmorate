package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MovieGenre {

    private Long id;
    private String name;

    public MovieGenre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MovieGenre() {
    }
}
