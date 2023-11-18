package ru.yandex.practicum.filmorate.dao.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendsStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistException {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        User thisUser = jdbcTemplate.queryForObject("SELECT user_id, email, login, name, birthday" +
                        " FROM users WHERE email=?",
                new UserMapper(), user.getEmail());
        //log.debug("Создан новый пользователь {}", thisUser);
        return thisUser;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        User thisUser = getUserById(user.getId());
        //log.debug("Данные пользователя {} успешно обновлены", thisUser);
        return thisUser;
    }

    @Override
    public List<User> findUsers() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users", new UserMapper());
        return users;
    }

    @Override
    public User getUserById(long id) {
        List<User> userList = jdbcTemplate.query("SELECT *" +
                " FROM users WHERE user_id=?", new UserMapper(), id);
        if(userList.isEmpty()) {
            throw new UserNotFoundException("Пользователь с таким id " + id + "не найден");
        }
        User thisUser = userList.get(0);
        for (long friendId : getUserFriends(id)) {
            thisUser.addFriend(friendId);
        }
        return thisUser;
    }

    public void addToFriends(User user1, User user2) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)",
                user1.getId(), user2.getId());
        user1.setFriends(getUserFriends(user1.getId()));
    }


//    public void addToFriends(long userId, long friendId) {
//        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)",
//                userId, friendId);
//        getUserById(userId).setFriends(getUserFriends(userId));
//    }

    public void dellFromFriends(User user1, User user2) {
        jdbcTemplate.update("DELETE FROM friends " +
                "WHERE user_id=? AND friend_id=?", user1.getId(), user2.getId());
        //log.debug("Пользователь {} удален из списка друзей пользователя {}", getUserById(friendId), getUserById(userId));
        user1.setFriends(getUserFriends(user1.getId()));
    }

    public Set<Long> getUserFriends(long id) {
        List<Long> friends = jdbcTemplate.queryForList("SELECT friend_id " +
                        "FROM friends WHERE user_id=?",
                Long.class, id);
        return new HashSet<>(friends);
    }

    public FriendsStatus showFriendshipStatus(User user1, User user2) {
        jdbcTemplate.queryForList("SELECT friend_id FROM friends" +
                " WHERE user_id=? AND friend_id=?", Long.class, user1.getId(), user2.getId());
        if(user1.getFriends().contains(user2.getId()) && user2.getFriends().contains(user1.getId()))
         {
            return FriendsStatus.CONFIRMED;
        }
        return FriendsStatus.UNCONFIRMED;
    }
}
