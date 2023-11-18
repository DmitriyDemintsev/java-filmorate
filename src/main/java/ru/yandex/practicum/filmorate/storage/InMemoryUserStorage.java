package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    protected long idGeneratorForUser = 0L;

    private long generateIdForUser() {
        return ++idGeneratorForUser;
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistException {
        user.setId(generateIdForUser());
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Создан новый пользователь с электронной почтой " + user.getEmail());
        } else {
            log.debug("Передаваемый id " + user.getId() + " уже есть в списке");
            throw new UserAlreadyExistException("Пользователь с таким id " + user.getId()
                    + " уже есть в списке");
        }
        return user;
    }

    @Override
    public User updateUser(User user) throws UserAlreadyExistException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлены данные пользователя с электронной почтой " + user.getEmail());
        } else {
            log.debug("Невозможно обновить данные, пользователь с id " + user.getId()
                    + " отсутствует в списке пользователей");
            throw new UserNotFoundException("Пользователь с таким id " + user.getId()
                    + " отсутствует в списке");
        }
        return user;
    }

    @Override
    public ArrayList<User> findUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с таким id " + id + "не найден");
        }
        return users.get(id);
    }

    @Override
    public void addToFriends(User user1, User user2) {
        user1.addFriend(user2.getId());
    }

    @Override
    public void dellFromFriends(User user1, User user2) {
        user1.removeFriend(user2.getId());
    }
}
