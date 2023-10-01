package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    protected UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void checkGetUsers() throws UserAlreadyExistException {
        LocalDate birthday_1 = LocalDate.of(1943, 1, 1);
        LocalDate birthday_2 = LocalDate.of(1954, 12, 31);
        User user_1 = new User(1, "test@test_1.com", "try_test_1", "Jim Beam", birthday_1);
        User user_2 = new User(2, "test@test_2.com", "try_test_2", "John Donne", birthday_2);

        userController.create(user_1);
        userController.create(user_2);

        List<User> users = userController.findUsers();
        assertEquals(users, List.of(user_1, user_2));
    }

    @Test
    void checkCreateUser() throws UserAlreadyExistException {
        LocalDate birthday_1 = LocalDate.now();
        LocalDate birthday_2 = LocalDate.of(2243, 1, 1);
        LocalDate birthday_3 = LocalDate.of(1946, 8, 20);
        User user_1 = new User(1, "test@test_1.com", "try_test_1", "Jim Beam", birthday_1);
        User user_2 = new User(2, "test@test_2.com", "try_test_2", null, birthday_1);
        User user_3 = new User(3, null, "try_test_3", "Jim Beam", birthday_1);
        User user_4 = new User(4, "testtest_4.com", "try_test_4", "Jim Beam", birthday_1);
        User user_5 = new User(5, "test@test_5.com", null, "Jim Beam", birthday_1);
        User user_6 = new User(6, "test@test_6.com", "try test_6", "Jim Beam", birthday_1);
        User user_7 = new User(7, "test@test_7.com", "try_test_7", "Jim Beam", birthday_2);
        User user_8 = new User(8, "test@test_1.com", "try_test_8", "Jim NickName", birthday_3);

        userController.create(user_1);
        userController.create(user_2);
        final InvalidEmailException emailException = assertThrows(InvalidEmailException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_3);
                    }
                });
        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_4);
                    }
                });

        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_8);
                    }
                });

        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_5);
                    }
                });

        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_6);
                    }
                });

        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user_7);
                    }
                });


        assertNotNull(user_1, "Пользователь не создан");
        assertEquals(user_2.getName(), user_2.getLogin());
        assertNotNull(user_4, "Пользователь с Email, не содержащим @, не создан");
        assertNotNull(user_5, "Пользователь с пустым логином не создан");
        assertNotNull(user_6, "Пользователь с логином, содержащим пробелы, не создан");
        assertNotNull(user_7, "Пользователь с невалидной датой рождения не создан");
    }

    @Test
    void checkPutUser() throws UserAlreadyExistException {
        LocalDate birthday_1 = LocalDate.of(1943, 1, 1);
        LocalDate birthday_2 = LocalDate.of(2017, 3, 23);
        User user = new User(1, "test@test.com", "oldLogin", "Jim Beam", birthday_1);
        userController.create(user);

        user.setEmail("test@test.com");
        user.setLogin("newLogin");
        user.setName("John Donne");
        user.setBirthday(birthday_2);

        userController.put(user);
        assertEquals(user.getEmail(), "test@test.com");
        assertEquals(user.getLogin(), "newLogin");
        assertEquals(user.getName(), "John Donne");
        assertEquals(user.getBirthday(), birthday_2);
    }
}
