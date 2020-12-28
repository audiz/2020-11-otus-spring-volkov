package ru.otus.work7.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Book;
import ru.otus.work7.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int i) throws SQLException {
        return new Book(rs.getLong("id"), rs.getString("title"),
                new Genre(rs.getLong("genreId"), rs.getString("genre")),
                new Author(rs.getLong("authorId"),  rs.getString("author")));
    }
}
