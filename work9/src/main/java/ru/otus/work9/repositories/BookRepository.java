package ru.otus.work9.repositories;

import ru.otus.work9.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> getAll();
    void insert(Book book);
    Book update(Book book);
    Optional<Book> getFullById(long id);
    Optional<Book> findById(long id);
    void delete(Book book);
    Long getCountByGenreId(long id);
    Long getCountByAuthorId(long id);
}
