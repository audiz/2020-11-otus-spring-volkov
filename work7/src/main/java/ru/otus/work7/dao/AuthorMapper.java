package ru.otus.work7.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Book;
import ru.otus.work7.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs, int i) throws SQLException {
        return new Author(rs.getLong("id"), rs.getString("name"));
    }
}
