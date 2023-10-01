package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    protected FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void checkGetFilms() {
        LocalDate releaseDate1 = LocalDate.of(1997, 3, 24);
        LocalDate releaseDate2 = LocalDate.of(2004, 7, 03);
        Film film1 = new Film(1, "Фильм_1", "Описание_1", releaseDate1, 120);
        Film film2 = new Film(2, "Фильм_2", "Описание_2", releaseDate2, 180);

        filmController.create(film1);
        filmController.create(film2);

        List<Film> films = filmController.findAll();
        assertEquals(films, List.of(film1, film2));
    }

    @Test
    void checkCreateFilm() {
        LocalDate releaseDate1 = LocalDate.of(1895, 12, 28);
        LocalDate releaseDate2 = LocalDate.of(1894, 7, 03);
        Film film1 = new Film(1, "Фильм_1", "Описание_1", releaseDate1, 120);
        Film film2 = new Film(2, null, "Описание_2", releaseDate1, 180);
        Film film3 = new Film(3, "Фильм_3", "Сортировка вставками — достаточно простой алгоритм. " + "Как в и любом другом, с увеличением размера сортируемого массива увеличивается и время сортировки. " + "Это предложение превосходит размер в 200 знаков на 14 символов.", releaseDate1, 210);
        Film film4 = new Film(4, "Фильм_4", "Описание_4", releaseDate2, 240);
        Film film5 = new Film(5, "Фильм_5", "Описание_5", releaseDate1, -273);

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
        Film film1 = new Film(1, "Фильм_1", "Описание_1", releaseDate1, 120);
        Film film2 = new Film(2, "Фильм_2", "Описание_2", releaseDate1, 180);
        filmController.create(film1);

        film1.setName("Обновили фильм");
        film1.setDescription("Обновили описание");
        film1.setReleaseDate(releaseDate2);
        film1.setDuration(180);

        filmController.put(film1);
        assertEquals(film1.getName(), "Обновили фильм");
        assertEquals(film1.getDescription(), "Обновили описание");
        assertEquals(film1.getReleaseDate(), releaseDate2);
        assertEquals(film1.getDuration(), 180);

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.put(film2);
            }
        });
        assertNotNull(film2, "Фильм не обновлен");
    }
}
