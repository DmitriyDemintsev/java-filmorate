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
        LocalDate releaseDate_1 = LocalDate.of(1997, 3, 24);
        LocalDate releaseDate_2 = LocalDate.of(2004, 7, 03);
        Film film_1 = new Film(1, "Фильм_1", "Описание_1", releaseDate_1, 120);
        Film film_2 = new Film(2, "Фильм_2", "Описание_2", releaseDate_2, 180);

        filmController.create(film_1);
        filmController.create(film_2);

        List<Film> films = filmController.findAll();
        assertEquals(films, List.of(film_1, film_2));
    }

    @Test
    void checkCreateFilm() {
        LocalDate releaseDate_1 = LocalDate.of(1895, 12, 28);
        LocalDate releaseDate_2 = LocalDate.of(1894, 7, 03);
        Film film_1 = new Film(1, "Фильм_1", "Описание_1", releaseDate_1, 120);
        Film film_2 = new Film(2, null, "Описание_2", releaseDate_1, 180);
        Film film_3 = new Film(3, "Фильм_3", "Сортировка вставками — достаточно простой алгоритм. " +
                "Как в и любом другом, с увеличением размера сортируемого массива увеличивается и время сортировки. " +
                "Это предложение превосходит размер в 200 знаков на 14 символов.", releaseDate_1, 210);
        Film film_4 = new Film(4, "Фильм_4", "Описание_4", releaseDate_2, 240);
        Film film_5 = new Film(5, "Фильм_5", "Описание_5", releaseDate_1, -273);

        filmController.create(film_1);
        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        filmController.create(film_2);
                    }
                });
        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        filmController.create(film_3);
                    }
                });
        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        filmController.create(film_4);
                    }
                });
        exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        filmController.create(film_5);
                    }
                });
    }

    @Test
    void checkPutFilm() {
        LocalDate releaseDate_1 = LocalDate.of(1997, 3, 24);
        LocalDate releaseDate_2 = LocalDate.of(2012, 7, 03);
        Film film_1 = new Film(1, "Фильм_1", "Описание_1", releaseDate_1, 120);
        Film film_2 = new Film(2, "Фильм_2", "Описание_2", releaseDate_1, 180);
        filmController.create(film_1);
        //filmController.create(film_2);

        film_1.setName("Обновили фильм");
        film_1.setDescription("Обновили описание");
        film_1.setReleaseDate(releaseDate_2);
        film_1.setDuration(180);

        filmController.put(film_1);
        assertEquals(film_1.getName(), "Обновили фильм");
        assertEquals(film_1.getDescription(), "Обновили описание");
        assertEquals(film_1.getReleaseDate(), releaseDate_2);
        assertEquals(film_1.getDuration(), 180);

        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        filmController.put(film_2);
                    }
                });
        assertNotNull(film_2, "Фильм не обновлен");
    }
}
