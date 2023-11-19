package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@TestPropertySource("classpath:test.properties")
@SpringBootTest
public class UserControllerTest {

    @Autowired
    protected UserController userController;

    @Test
    void checkGetUsers() throws UserAlreadyExistException {
        LocalDate birthday1 = LocalDate.of(1943, 1, 1);
        LocalDate birthday2 = LocalDate.of(1954, 12, 31);
        User user9 = new User(0L, "test@test_9.com", "try_test_9", "Jim Beam", birthday1);
        User user10 = new User(0L, "test@test_10.com", "try_test_10", "John Donne", birthday2);
        System.out.println("СМОТРИМ " + user9);
        System.out.println("СМОТРИМ " + user10);

        user9 = userController.create(user9);
        user10 = userController.create(user10);
        System.out.println("СМОТРИМ создание " + user9);
        System.out.println("СМОТРИМ создание " + user10);

        List<User> users = userController.findUsers();

        //users.add(user9);
        //users.add(user10);
        System.out.println("СМОТРИМ users" + users);

        assertEquals(users, userController.findUsers());
    }

    @Test
    void checkCreateUser() throws UserAlreadyExistException {
        LocalDate birthday1 = LocalDate.now();
        LocalDate birthday2 = LocalDate.of(2243, 1, 1);
        LocalDate birthday3 = LocalDate.of(1946, 8, 20);
        User user1 = new User(0L, "test@test_1.com", "try_test_1", "Jim Beam", birthday1);
        User user2 = new User(0L, "test@test_2.com", "try_test_2", null, birthday1);
        User user3 = new User(0L, null, "try_test_3", "Jim Beam", birthday1);
        User user4 = new User(0L, "testtest_4.com", "try_test_4", "Jim Beam", birthday1);
        User user5 = new User(0L, "test@test_5.com", null, "Jim Beam", birthday1);
        User user6 = new User(0L, "test@test_6.com", "try test_6", "Jim Beam", birthday1);
        User user7 = new User(0L, "test@test_7.com", "try_test_7", "Jim Beam", birthday2);
        User user8 = new User(0L, "test@test_1.com", "try_test_8", "Jim NickName", birthday3);

        user1 = userController.create(user1);
        user2 = userController.create(user2);
        assertThrows(InvalidEmailException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user3);
                    }
                });

        assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user4);
                    }
                });

        assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user8);
                    }
                });

        assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user5);
                    }
                });

        assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user6);
                    }
                });

        assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws UserAlreadyExistException {
                        userController.create(user7);
                    }
                });

        assertNotNull(user1, "Пользователь не создан");
        assertEquals(user2.getName(), user2.getLogin());
        assertNotNull(user4, "Пользователь с Email, не содержащим @, не создан");
        assertNotNull(user5, "Пользователь с пустым логином не создан");
        assertNotNull(user6, "Пользователь с логином, содержащим пробелы, не создан");
        assertNotNull(user7, "Пользователь с невалидной датой рождения не создан");
    }

    @Test
    void checkPutUser() throws UserAlreadyExistException {
        LocalDate birthday1 = LocalDate.of(1943, 1, 1);
        LocalDate birthday2 = LocalDate.of(2017, 3, 23);
        User user = new User(0L, "test@test.com", "oldLogin", "Jim Beam", birthday1);
        User firstUser = userController.create(user);

        firstUser.setEmail("test@test.com");
        firstUser.setLogin("newLogin");
        firstUser.setName("John Donne");
        firstUser.setBirthday(birthday2);

        User updateUser = userController.update(firstUser);
        assertEquals(updateUser.getEmail(), "test@test.com");
        assertEquals(updateUser.getLogin(), "newLogin");
        assertEquals(updateUser.getName(), "John Donne");
        assertEquals(updateUser.getBirthday(), birthday2);
    }
}
