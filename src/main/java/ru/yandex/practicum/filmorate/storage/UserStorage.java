package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    protected int idGeneratorForUser = 0;

    private int generateIdForUser() {
        return ++idGeneratorForUser;
    }

    public User create(User user) throws UserAlreadyExistException {
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

    public User put(User user) throws UserAlreadyExistException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлены данные пользователя с электронной почтой " + user.getEmail());
        } else {
            log.debug("Невозможно обновить данные, пользователь с id " + user.getId()
                    + " отсутствует в списке пользователей");
            throw new UserAlreadyExistException("Пользователь с таким id " + user.getId()
                    + " отсутствует в списке");
        }
        return user;
    }

    public ArrayList<User> findUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }
}
