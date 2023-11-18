package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {

    private Long id;
    private String name;

    public MPA(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MPA() {
    }

//    G, // нет возрастных ограничений
//    PG, // детям рекомендуется смотреть фильм с родителями
//    PG-13, // детям до 13 лет просмотр не желателен
//    R, // лицам до 17 лет просматривать фильм можно только в присутствии взрослого
//    NC-17 // лицам до 18 лет просмотр запрещён
}
