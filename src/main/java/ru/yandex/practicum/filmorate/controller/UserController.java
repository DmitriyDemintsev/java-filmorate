package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) throws UserAlreadyExistException {
        validateUser(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws UserNotFoundException {
        userService.getUserById(user.getId());
        validateUser(user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.addToFriends(userService.getUserById(id), userService.getUserById(friendId));
    }

    @GetMapping
    public List<User> findUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable("id") long id) {
        return userService.getUserFriends(userService.getUserById(id)).stream()
                .map(userService::getUserById)
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findListOfMutualFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return userService.findListOfMutualFriends(userService.getUserById(id),
                        userService.getUserById(otherId)).stream()
                .map(userService::getUserById)
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void dellFromFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.dellFromFriends(userService.getUserById(id), userService.getUserById(friendId));
    }

    private void validateUser(User user) {
        LocalDate data = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.debug("Пользователь не предоставил данные электронной почты");
            throw new InvalidEmailException("");
        }
        if (!user.getEmail().contains("@")) {
            log.debug("Ошибка в написании адреса электронной почты");
            throw new ValidationException("Адрес электронной почты указан без символа @");
        }
        for (User entry : userService.findAllUsers()) {
            if (user.getEmail().equals(entry.getEmail())
                    && user.getId() != entry.getId()) {
                log.debug("Дублирующийся адрес Email");
                throw new ValidationException("Данный адрес электронной почты " +
                        "принадлежит одному из пользователей");
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
