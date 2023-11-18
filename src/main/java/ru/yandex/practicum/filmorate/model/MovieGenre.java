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

//    1Комедия, // боевик
//    2 Драма, // приключения
//    3 BIOPICS, // фильм-биография
//    4 Документальный, // мультфильмы
//    5 CLASSIC_FILM, // классическое кино
//    6 Мультфильм, // комедия
//    7 CRIMINAL_DRAMA, // криминальная драма
//    8 DETECTIVE, // детектив
//    9 DISASTER, // фильм-катастрофа
//    10 DOCUMENTARY, // документальный фильм
//    11 FAMILY_ORIENTED_FILM, // фильм для всей семьи
//    12 Триллер, // фантастика
//    13 HISTORICAL, // исторический фильм
//    14 HORROR, // ужасы
//    15 MUSICAL, // мюзикл
//    16 ROMANTIC_Мультфильм, // романтическая комедия
//    17 SCIENCE_FICTION, // научная фантастика
//    18 THRILLER, // триллер
//    19 WAR_FILM, // военный фильм
//    20 WESTERN, // вестерн
}
