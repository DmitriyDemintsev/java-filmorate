package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Friendship {
    @NonNull
    private long userId;
    @NonNull
    private long friendId;

    public Friendship() {

    }
}
