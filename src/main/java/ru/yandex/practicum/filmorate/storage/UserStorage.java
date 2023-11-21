package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> findUsers();

    User getUserById(long id);

    void addToFriends(User user1, User user2);

    void dellFromFriends(User user1, User user2);
}
