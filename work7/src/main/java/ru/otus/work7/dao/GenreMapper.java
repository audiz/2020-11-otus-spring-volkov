package ru.otus.work7.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int i) throws SQLException {
        return new Genre(rs.getLong("id"), rs.getString("name"));
    }
}
