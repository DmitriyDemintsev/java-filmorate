package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@TestPropertySource("classpath:test.properties")
@SpringBootTest
public class FilmControllerTest {

    @Autowired
    protected FilmController filmController;

    private MPA mpa = new MPA(1L, "G");

    @Test
    void checkGetFilms() {
        LocalDate releaseDate1 = LocalDate.of(1997, 3, 24);
        LocalDate releaseDate2 = LocalDate.of(2004, 7, 03);
        Film film1 = new Film(0L, "Фильм_1", "Описание_1", releaseDate1, 120, mpa);
        Film film2 = new Film(0L, "Фильм_2", "Описание_2", releaseDate2, 180, mpa);

        List<Film> films = filmController.findFilms();

        film1 = filmController.create(film1);
        film2 = filmController.create(film2);

        films.add(film1);
        films.add(film2);

        assertEquals(films, filmController.findFilms());
    }

    @Test
    void checkCreateFilm() {
        LocalDate releaseDate1 = LocalDate.of(1895, 12, 28);
        LocalDate releaseDate2 = LocalDate.of(1894, 7, 03);
        Film film1 = new Film(0L, "Фильм_3", "Описание_1", releaseDate1, 120, mpa);
        Film film2 = new Film(0L, null, "Описание_2", releaseDate1, 180, mpa);
        Film film3 = new Film(0L, "Фильм_4", "Сортировка вставками — достаточно простой алгоритм. "
                + "Как в и любом другом, с увеличением размера сортируемого массива увеличивается и время сортировки. "
                + "Это предложение превосходит размер в 200 знаков на 14 символов.", releaseDate1, 210, mpa);
        Film film4 = new Film(0L, "Фильм_5", "Описание_4", releaseDate2, 240, mpa);
        Film film5 = new Film(0L, "Фильм_6", "Описание_5", releaseDate1, -273, mpa);

        filmController.create(film1);
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film2);
            }
        });
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film3);
            }
        });
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film4);
            }
        });
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film5);
            }
        });
    }

    @Test
    void checkPutFilm() {
        LocalDate releaseDate1 = LocalDate.of(1997, 3, 24);
        LocalDate releaseDate2 = LocalDate.of(2012, 7, 03);
        Film film1 = new Film(0L, "Фильм_7", "Описание_1", releaseDate1, 120, mpa);
        Film film2 = new Film(0L, "Фильм_8", "Описание_2", releaseDate1, 180, mpa);
        film1 = filmController.create(film1);

        film1.setName("Обновили фильм");
        film1.setDescription("Обновили описание");
        film1.setReleaseDate(releaseDate2);
        film1.setDuration(180);

        filmController.put(film1);
        assertEquals(film1.getName(), "Обновили фильм");
        assertEquals(film1.getDescription(), "Обновили описание");
        assertEquals(film1.getReleaseDate(), releaseDate2);
        assertEquals(film1.getDuration(), 180);

        assertThrows(FilmNotFoundException.class, new Executable() {
            @Override
            public void execute() throws FilmNotFoundException {
                filmController.put(film2);
            }
        });
        assertNotNull(film2, "Фильм не обновлен");
    }
}
