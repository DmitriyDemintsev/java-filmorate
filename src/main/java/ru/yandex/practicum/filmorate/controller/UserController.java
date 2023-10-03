package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserStorage userStorage = new UserStorage();

    @PostMapping
    public User create(@RequestBody User user) throws UserAlreadyExistException {
        validateUser(user);
        return userStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) throws UserAlreadyExistException {
        validateUser(user);
        return userStorage.put(user);
    }

    @GetMapping
    public ArrayList<User> findUsers() {
        return userStorage.findUsers();
    }

    public void validateUser(@RequestBody User user) {
        LocalDate data = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.debug("Пользователь не предоставил данные электронной почты");
            throw new InvalidEmailException("");
        }
        if (!user.getEmail().contains("@")) {
            log.debug("Ошибка в написании адреса электронной почты");
            throw new ValidationException("Адрес электронной почты указан без символа @");
        }
        for (User entry : userStorage.findUsers()) {
            if (user.getEmail().equals(entry.getEmail())
                    && user.getId() != entry.getId()) {
                log.debug("Дублирующийся адрес Email");
                throw new ValidationException("Данный адрес электронной почты принадлежит одному из пользователей");
            }
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Переданный логин пуст или содержит пробелы");
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(data)) {
            log.debug("Передается еще не существующая дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
