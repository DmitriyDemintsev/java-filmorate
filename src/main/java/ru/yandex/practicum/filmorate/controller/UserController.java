package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    protected int idGeneratorForUser = 0;

    private int generateIdForUser() {
        return ++idGeneratorForUser;
    }

    @PostMapping
    public User create(@RequestBody User user) throws UserAlreadyExistException {
        validateUser(user);
        user.setId(generateIdForUser());
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Создан новый пользователь с электронной почтой " + user.getEmail());
        } else {
            log.debug("Передаваемый id " + user.getId() + " уже есть в списке");
            throw new UserAlreadyExistException("Прльзователь с таким id " + user.getId()
                    + " уже есть в списке");
        }
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) throws UserAlreadyExistException {
        validateUser(user);
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

    @GetMapping
    public ArrayList<User> findUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    protected void validateUser(@RequestBody User user) {
        LocalDate data = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.debug("Пользователь не предоставил данные электронной почты");
            throw new InvalidEmailException("");
        }
        if (!user.getEmail().contains("@")) {
            log.debug("Ошибка в написании адреса электронной почты");
            throw new ValidationException("Адрес электронной почты указан без символа @");
        }
        for (Map.Entry<Integer, User> entry: users.entrySet()) {
            if (user.getEmail().equals(entry.getValue().getEmail())
                    && user.getId() != entry.getKey()) {
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
