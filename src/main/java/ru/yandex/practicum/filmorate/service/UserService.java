package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(User user1, User user2) {
        if (!user1.getFriends().contains(user2.getId())) {
            userStorage.addToFriends(user1, user2);
        } else {
            throw new UserAlreadyExistException("Пользователь с таким id " + user2.getId()
                    + " уже есть в списке ваших друзей");
        }
    }

    public void dellFromFriends(User user1, User user2) {
        if (user1.getFriends().contains(user2.getId())) {
            userStorage.dellFromFriends(user1, user2);
        } else {
            throw new UserAlreadyExistException("Пользователя с таким id " + user2.getId()
                    + " нет в списке ваших друзей");
        }
    }

    public List<Long> findListOfMutualFriends(User user1, User user2) {
        List<Long> mutualFriends = new ArrayList<>();

        for (long i : user1.getFriends()) {
            for (long j : user2.getFriends()) {
                if (i == j) {
                    mutualFriends.add(i);
                }
            }
        }
        return mutualFriends;
    }

    public Set<Long> getUserFriends(User user) {
        return user.getFriends();
    }

    @Qualifier("UserDbStorage")
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Qualifier("UserDbStorage")
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Qualifier("UserDbStorage")
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Qualifier("UserDbStorage")
    public List<User> findAllUsers() {
        return userStorage.findUsers();
    }
}
