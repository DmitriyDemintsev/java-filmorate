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
import java.util.Set;

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
        System.out.println("!!!!!" + users);
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

//    @Test
//    void checkAddToFriends() {
//        User firstUser = new User(0L, "first@email.ru", "vanya", "Ivan Ivanov",
//                LocalDate.of(1991, 1, 1));
//        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
//                LocalDate.of(1992, 2, 2));
//
//        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//        firstUser = userStorage.createUser(firstUser);
//        sekondUser = userStorage.createUser(sekondUser);
//
//        userStorage.addToFriends(firstUser, sekondUser);
//
//        assertThat(firstUser.getFriends().size())
//                .isEqualTo(1);
//        System.out.println(firstUser.getFriends());
//        assertThat(2L) //тут валится без аннотации, в пакетном прогоне не 2L, а 9L
//                .isIn(firstUser.getFriends());
//    }
//
//    @Test
//    void checkDeleteFromFriends() {
//     User firstUser = new User(0L, "first@email.ru", "vanya", "Ivan Ivanov",
//             LocalDate.of(1991, 1, 1));
//     User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
//             LocalDate.of(1992, 2, 2));
//
//     UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//     firstUser = userStorage.createUser(firstUser);
//     sekondUser = userStorage.createUser(sekondUser);
//
//     userStorage.addToFriends(firstUser, sekondUser);
//     userStorage.addToFriends(sekondUser, firstUser);
//
//     userStorage.deleteFromFriends(firstUser, sekondUser);
//     userStorage.deleteFromFriends(sekondUser, firstUser);
//
//     assertThat(firstUser.getFriends().size())
//             .isEqualTo(0);
//     assertThat(sekondUser.getFriends())
//             .isNotNull();
//    }
//
//    @Test
//    void checkGetUserFriends() {
//        User firstUser = new User(0L, "first@email.ru", "vanya", "Ivan Ivanov",
//                LocalDate.of(1991, 1, 1));
//        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
//                LocalDate.of(1992, 2, 2));
//        User therdUser = new User(0L, "therd@email.ru", "kolya", "Nikolay Nikolaev",
//                LocalDate.of(1993, 3, 3));
//
//        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//        firstUser = userStorage.createUser(firstUser);
//        sekondUser = userStorage.createUser(sekondUser);
//        therdUser = userStorage.createUser(therdUser);
//
//        userStorage.addToFriends(firstUser, sekondUser);
//        userStorage.addToFriends(firstUser, therdUser);
//
//        //userStorage.getUserFriends(firstUser.getId());
//
//        Set<Long> friends = userStorage.getUserFriends(firstUser.getId());
//
//        assertThat(friends.size())
//                .isEqualTo(2);
//    }
//
//    @Test
//    void checkShowFriendshipStatus() {
//        User firstUser = new User(0L, "first@email.ru", "vanya", "Ivan Ivanov",
//                LocalDate.of(1991, 1, 1));
//        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
//                LocalDate.of(1992, 2, 2));
//        User therdUser = new User(0L, "therd@email.ru", "kolya", "Nikolay Nikolaev",
//                LocalDate.of(1993, 3, 3));
//
//        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//        firstUser = userStorage.createUser(firstUser);
//        sekondUser = userStorage.createUser(sekondUser);
//        therdUser = userStorage.createUser(therdUser);
//        System.out.println(firstUser);
//        System.out.println(sekondUser);
//        System.out.println(therdUser);
//
//        userStorage.addToFriends(firstUser, sekondUser);
//        userStorage.addToFriends(firstUser, therdUser);
//        userStorage.addToFriends(sekondUser, firstUser);
//        userStorage.addToFriends(therdUser, sekondUser);
//
//        userStorage.showFriendshipStatus(firstUser, sekondUser);
//        userStorage.showFriendshipStatus(firstUser, therdUser);
//        userStorage.showFriendshipStatus(sekondUser, firstUser);
//        userStorage.showFriendshipStatus(therdUser, sekondUser);
//
//        assertThat(firstUser.getFriendsStatus())
//                .isEqualTo(sekondUser.getFriendsStatus());
//        assertThat(firstUser.getFriendsStatus())
//                .isEqualTo(therdUser.getFriendsStatus());
//    }
}
