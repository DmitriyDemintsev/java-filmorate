package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MovieGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<MovieGenre> {
    @Override
    public MovieGenre mapRow(ResultSet rs, int rowNum) throws SQLException {

        MovieGenre movieGenres = new MovieGenre();
        movieGenres.setId(rs.getLong("genre_id"));
        movieGenres.setName(rs.getString("name"));
        return movieGenres;
    }
}
