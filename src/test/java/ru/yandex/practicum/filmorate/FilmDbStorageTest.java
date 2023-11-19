package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.db.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MovieGenre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private MPA mpa = new MPA(1L, "G");

    @Test
    void checkCreateFilm() {
        Film film = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Film newFilm = filmStorage.createFilm(film);

        assertThat(newFilm)
                .isNotNull();
    }

    @Test
    void checkUpdateFilm() {
        Film newFilm = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 1, 01), 58, mpa);

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        newFilm = filmStorage.createFilm(newFilm);

        Film testFilm = new Film(newFilm.getId(), "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 06, 18), 106, mpa);

        newFilm = filmStorage.updateFilm(testFilm);

        assertThat(testFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void checkFindFilms() {
        Film firstFilm = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);
        Film secondFilm = new Film(0L, "Люди в чёрном", "Научно-фантастический комедийный боевик",
                LocalDate.of(1997, 7, 03), 98, mpa);
        Film therdFilm = new Film(0L, "Корпорация монстров",
                "Мы считаем, что они страшные, но на самом деле их пугаем мы!",
                LocalDate.of(2001, 10, 28), 92, mpa);

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.createFilm(firstFilm);
        filmDbStorage.createFilm(secondFilm);
        filmDbStorage.createFilm(therdFilm);

        List<Film> films = filmDbStorage.findFilms();

        assertThat(films)
                .isNotNull();
        assertThat(films.size())
                .isEqualTo(3);
    }

    @Test
    void checkGetFilmById() {
        Film newFilm = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        newFilm = filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.getFilmById(newFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void checkAddLike() {
        Film film = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        User firstUser = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));
        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
                LocalDate.of(1992, 2, 2));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        firstUser = userStorage.createUser(firstUser);
        sekondUser = userStorage.createUser(sekondUser);

        filmStorage.addLike(film, firstUser);
        filmStorage.addLike(film, sekondUser);

        assertThat(film.getLikes())
                .isNotNull();
        assertThat(film.getLikes().size())
                .isEqualTo(2);
    }

    @Test
    void checkDeleteLike() {
        Film film = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        User firstUser = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));
        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
                LocalDate.of(1992, 2, 2));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        firstUser = userStorage.createUser(firstUser);
        sekondUser = userStorage.createUser(sekondUser);

        filmStorage.addLike(film, firstUser);
        filmStorage.addLike(film, sekondUser);

        filmStorage.dellLike(film, firstUser);
        filmStorage.dellLike(film, sekondUser);

        assertThat(film.getLikes().size())
                .isEqualTo(0);
    }

    @Test
    void checkGetFilmLikes() {
        Film film = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        User firstUser = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));
        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
                LocalDate.of(1992, 2, 2));
        User therdUser = new User(0L, "therd@email.ru", "kolya", "Nikolay Nikolaev",
                LocalDate.of(1993, 3, 3));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        firstUser = userStorage.createUser(firstUser);
        sekondUser = userStorage.createUser(sekondUser);
        therdUser = userStorage.createUser(therdUser);

        filmStorage.addLike(film, firstUser);
        filmStorage.addLike(film, sekondUser);
        filmStorage.addLike(film, therdUser);

        Set<Long> likes = filmStorage.getFilmLikes(film);

        assertThat(likes.size())
                .isEqualTo(3);
    }

    @Test
    void checkGetLikesQuantity() {
        Film film = new Film(0L, "Форсаж", "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18), 106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        User firstUser = new User(0L, "user@email.ru", "vanya123", "Ivan Ivanov",
                LocalDate.of(1990, 1, 1));
        User sekondUser = new User(0L, "second@email.ru", "petya", "Petr Petrov",
                LocalDate.of(1992, 2, 2));
        User therdUser = new User(0L, "therd@email.ru", "kolya", "Nikolay Nikolaev",
                LocalDate.of(1993, 3, 3));

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        firstUser = userStorage.createUser(firstUser);
        sekondUser = userStorage.createUser(sekondUser);
        therdUser = userStorage.createUser(therdUser);

        filmStorage.addLike(film, firstUser);
        filmStorage.addLike(film, sekondUser);
        filmStorage.addLike(film, therdUser);

        filmStorage.getLikesQuantity(film);

        assertThat(film.getLikes().size())
                .isEqualTo(3);
    }

    @Test
    void checkAddGenreToFilm() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addGenreToFilm(film, new MovieGenre(1L, "Комедия"));

        assertThat(film.getGenres().size())
                .isEqualTo(1);
    }

    @Test
    void checkDeleteGenre() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addGenreToFilm(film, new MovieGenre(1L, "Комедия"));
        filmStorage.addGenreToFilm(film, new MovieGenre(6L, "Мультфильм"));
        filmStorage.deleteGenreFromMovie(film, new MovieGenre(1L, "Комедия"));

        assertThat(film.getGenres().size())
                .isEqualTo(1);
    }

    @Test
    void checkDeleteAllGenres() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addGenreToFilm(film, new MovieGenre(1L, "Комедия"));
        filmStorage.addGenreToFilm(film, new MovieGenre(6L, "Мультфильм"));
        filmStorage.deleteAllMovieGenres(film);

        assertThat(film.getGenres().size())
                .isEqualTo(0);
    }

    @Test
    void checkGetFilmGenres() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage((jdbcTemplate));
        film = filmStorage.createFilm(film);
        filmStorage.addGenreToFilm(film, new MovieGenre(1L, "Комедия"));
        filmStorage.addGenreToFilm(film, new MovieGenre(6L, "Мультфильм"));
        filmStorage.addGenreToFilm(film, new MovieGenre(2L, "Драма"));

        assertThat(filmStorage.getFilmGenres(film).size())
                .isEqualTo(3);
    }

    @Test
    void checkShowFilmGenres() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addGenreToFilm(film, new MovieGenre(1L, "Комедия"));
        filmStorage.addGenreToFilm(film, new MovieGenre(6L, "Мультфильм"));

        assertThat(filmStorage.showFilmGenres(film).size())
                .isEqualTo(2);
    }

    @Test
    void checkAddMPARating() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addMPARating(film, new MPA(5L, "NC-17"));

        assertThat(film.getMpa())
                .isEqualTo(new MPA(5L, "NC-17"));
    }

    @Test
    void checkGetMPARating() {
        Film film = new Film(0L, "Форсаж",
                "Первая часть франшизы «Форсаж»",
                LocalDate.of(2001, 6, 18),
                106, mpa);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        film = filmStorage.createFilm(film);

        filmStorage.addMPARating(film, new MPA(5L, "NC-17"));
        assertThat(filmStorage.getIdMPARating(film))
                .isEqualTo(5);
    }

    @Test
    void checkGetMpaRatingById() {
        FilmDbStorage filmStorage = new FilmDbStorage((jdbcTemplate));

        assertThat(filmStorage.getMpaRatingById(1L))
                .isEqualTo(new MPA(1L, "G"));
    }
}
