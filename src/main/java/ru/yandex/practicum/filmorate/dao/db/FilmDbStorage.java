package ru.yandex.practicum.filmorate.dao.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MovieGenre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.*;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        jdbcTemplate.update("INSERT INTO films (name, description, releaseDate, duration, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        Film thisFilm = jdbcTemplate.queryForObject("SELECT * FROM films WHERE name=?",
                new FilmMapper(), film.getName());
        thisFilm.setMpa(getMpaRatingById(thisFilm.getMpa().getId()));
        for (MovieGenre genre: film.getGenres()) {
            addGenreToFilm(thisFilm, genre);
        }
        thisFilm.setGenres(getFilmGenres(thisFilm));
        log.debug("Создан новый фильм {}", thisFilm);
        return thisFilm;
    };

    @Override
    public Film updateFilm(Film film){
        jdbcTemplate.update("UPDATE films SET name=?, description=?, releaseDate=?, " +
                        "duration=?, mpa_id=?" +
                "WHERE film_id=?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        Film thisFilm = getFilmById(film.getId());
        deleteAllMovieGenres(thisFilm);
        for (MovieGenre genre: film.getGenres()) {
            addGenreToFilm(thisFilm, genre);
        }
        thisFilm.setGenres(getFilmGenres(thisFilm));
        return thisFilm;
    };

    @Override
    public List<Film> findFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films", new FilmMapper());
        for (Film film: films) {
            film.setMpa(getMpaRatingById(film.getMpa().getId()));
            film.setLikes(getFilmLikes(film));
            for (MovieGenre genreId: getFilmGenres(film)) {
                film.addGenre(genreId);
            }

        }
        return films;
    };

    @Override
    public Film getFilmById(long id) {
        List<Film> filmList = jdbcTemplate.query("SELECT * FROM films " +
                "WHERE film_id=?", new FilmMapper(), id);
        if (filmList.isEmpty()) {
            throw new FilmNotFoundException("Фильм, который вы хотите обновить, отсутствует в вашей коллекции");
        }
        Film thisFilm = filmList.get(0);
        for (MovieGenre genreId: getFilmGenres(thisFilm)) {
            thisFilm.addGenre(genreId);
        }
        thisFilm.setMpa(getMpaRatingById(thisFilm.getMpa().getId()));
        thisFilm.setLikes(getFilmLikes(thisFilm));
        return thisFilm;
    };

    public void addLike(Film film, User user) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", film.getId(), user.getId());
        log.debug("Фильм {} отмечен как понравившийся", getFilmById(film.getId()));
        film.setLikes(getFilmLikes(film));
    }

    public void dellLike(Film film, User user) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", film.getId(), user.getId());
        film.setLikes(getFilmLikes(film));
    }

    public Set<Long> getFilmLikes(Film film) {
        List<Long> likes = jdbcTemplate.queryForList("SELECT user_id FROM likes WHERE film_id=?",
                Long.class, film.getId());
        return new HashSet<>(likes);
    }

    public int getLikesQuantity(Film film) {
        int likesQuantity = jdbcTemplate.queryForObject("SELECT COUNT(user_id) FROM likes WHERE film_id=?",
                Integer.class, film.getId());
        return likesQuantity;
    }

    public Long getIdByGenreName(String nameOfGenre) {
        long idGenre = jdbcTemplate.queryForObject("SELECT genre_id FROM genres WHERE name=?",
                Long.class, nameOfGenre);
        return idGenre;
    }

    public MovieGenre getGenreNameById(long id) {
        List<MovieGenre> nameOfGenre = jdbcTemplate.query("SELECT * " +
                        "FROM genres WHERE genre_id=?", new GenreMapper(), id);
        if (nameOfGenre.isEmpty()) {
            throw new GenreNotFoundException("Жанр не найден");
        }
        return nameOfGenre.get(0);
    }

    public Set<MovieGenre> getFilmGenres(Film film) {
        List<MovieGenre> genres = jdbcTemplate.query("SELECT * " +
                        "FROM film_genre AS fg LEFT JOIN genres AS g ON fg.genre_id=g.genre_id " +
                        "WHERE film_id=? ORDER BY g.genre_id",
                new GenreMapper(), film.getId());
        return new HashSet<>(genres);
    }

    public void addGenreToFilm(Film film, MovieGenre genre) {
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?)",
                film.getId(), genre.getId());
        film.setGenres(getFilmGenres(film));
    }

    public void deleteGenreFromMovie(Film film, MovieGenre genre) {
        jdbcTemplate.update("DELETE FROM film_genre " +
                        "WHERE film_id=? AND genre_id=?",
                film.getId(), genre.getId());
        film.setGenres(getFilmGenres(film));
    }

    public void deleteAllMovieGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?",
                film.getId());
        film.setGenres(getFilmGenres(film));
    }

    public Set<MovieGenre> showFilmGenres(Film film) {
        List<MovieGenre> filmGenres = jdbcTemplate.query("SELECT * FROM film_genre AS fg " +
                        "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id WHERE film_id=?",
                new GenreMapper(), film.getId());
        return new HashSet<>(filmGenres);
    }

    public List<MovieGenre> getGenresFromTable() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY genre_id",
                new GenreMapper());
    }

    public List<MPA> getMpaFromTable() {
        return jdbcTemplate.query("SELECT * FROM mpa ORDER BY mpa_id",
                new MPAMapper());
    }

    public Long getIdByMpaRating(MPA nameOfMpaRating) {
        Long mpaId = jdbcTemplate.queryForObject("SELECT mpa_id FROM mpa WHERE name=?",
                Long.class, nameOfMpaRating.getName());
        return mpaId;
    }

    public MPA getMpaRatingById(Long id) {
        List<MPA> nameOfMpaRating = jdbcTemplate.query("SELECT * " +
                        "FROM mpa WHERE mpa_id=?",
                new MPAMapper(), id);
        if (nameOfMpaRating.isEmpty()) {
            throw new MPANotFoundException("Такой рейтинг не найден");
        }
        return nameOfMpaRating.get(0);
    }

    public void addMPARating(Film film, MPA nameOfMpaRating) {
        jdbcTemplate.update("UPDATE films SET mpa_id=? WHERE film_id=?",
                getIdByMpaRating(nameOfMpaRating), film.getId());
        film.setMpa(nameOfMpaRating);
    }

    public int getIdMPARating(Film film) {
        int MPARating = jdbcTemplate.queryForObject("SELECT mpa_id " +
                "FROM films WHERE film_id=?", Integer.class, film.getId());
        return MPARating;
    }

//    public int setMpaId(int id) {
//
//    }
}

//    public Film updateGenre(Film film, String nameOfGenre) {
//        Film newFilm = deleteGenre(film, getGenreNameById(film.getId()));
//        film = addGenreToFilm(newFilm, nameOfGenre);
//        log.debug("Обновлены сведения о жанрах для фильма {}", getFilmById(film.getId()));
//    }