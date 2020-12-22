package ru.otus.work7.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.work7.domain.Book;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations)
    {
        this.jdbc = jdbcOperations;
    }

    @Override
    public List<Book> getAll() {
        return jdbc.getJdbcOperations().query("select `books`.`id`, `books`.`title`, `books`.`genre` as genreId, `books`.`author` as authorId," +
                " `genres`.`name` as genre, `authors`.`name` as author from books INNER JOIN genres ON `books`.`genre` = `genres`.`id` " +
                " INNER JOIN authors ON `books`.`author` = `authors`.`id`", new BookMapper());
    }

    @Override
    public void insert(Book book) {
        jdbc.update("insert into books(`id`, `title`, `genre`, `author`) values(:id, :title, :genre, :author)",
                Map.of("id", book.getId(), "title", book.getTitle(), "genre", book.getGenre().getId(), "author", book.getAuthor().getId()));
    }

    @Override
    public Book getById(long id) {
        try {
            return jdbc.queryForObject("select `books`.`id`, `books`.`title`, `books`.`genre` as genreId, `books`.`author` as authorId, " +
                    "`genres`.`name` as genre, `authors`.`name` as author from books INNER JOIN genres ON `books`.`genre` = `genres`.`id` " +
                    "INNER JOIN authors ON `books`.`author` = `authors`.`id` WHERE `books`.`id` = :id", Map.of("id", id), new BookMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public int update(Book book) {
        return jdbc.update("update books set `title` = :title, `genre` = :genre,  `author` = :author where `id` = :id",
                Map.of("id", book.getId(), "title", book.getTitle(), "genre", book.getGenre().getId(), "author", book.getAuthor().getId()));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from books where `id` = :id", Map.of("id", id));
    }

    @Override
    public int getCountGenreById(long id) {
        return jdbc.queryForObject("select count(*) from books WHERE genre = :id", Map.of("id", id), Integer.class);
    }

    @Override
    public int getCountAuthorById(long id) {
        return jdbc.queryForObject("select count(*) from books WHERE author = :id",  Map.of("id", id), Integer.class);
    }
}
