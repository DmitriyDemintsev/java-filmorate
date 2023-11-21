package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.db.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    void checkCreateUser() throws UserAlreadyExistException {
        User user = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = userStorage.createUser(user);

        assertThat(newUser)
                .isNotNull();
    }

    @Test
    void checkUpdateUser() {
        User newUser = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        newUser = userStorage.createUser(newUser);

        User testUser = new User(newUser.getId(), "user@email.ru", "petya", "Petya Petrov",
                LocalDate.of(1999, 3, 3));

        newUser = userStorage.updateUser(testUser);

        assertThat(testUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void checkFindUsers() {
        User firstUser = new User(0L, "first@email.ru", "vanya", "Ivan Ivanov",
                LocalDate.of(1991, 1, 1));
        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
                LocalDate.of(1992, 2, 2));
        User therdUser = new User(0L, "therd@email.ru", "kolya", "Nikolay Nikolaev",
                LocalDate.of(1993, 3, 3));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(firstUser);
        userStorage.createUser(sekondUser);
        userStorage.createUser(therdUser);

        List<User> users = userStorage.findUsers();

        assertThat(users)
                .isNotNull();
        assertThat(users.size())
                .isEqualTo(3);
    }

    @Test
    void checkGetUserById() {
        User newUser = new User(0L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        newUser = userStorage.createUser(newUser);

        User savedUser = userStorage.getUserById(newUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }
}
