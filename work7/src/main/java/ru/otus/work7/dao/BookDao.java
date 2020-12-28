package ru.otus.work7.dao;

import ru.otus.work7.domain.Book;

import java.util.List;

public interface BookDao {
    List<Book> getAll();
    void insert(Book book);
    Book getById(long id);
    int update(Book book);
    int deleteById(long id);
    int getCountGenreById(long id);
    int getCountAuthorById(long id);
}
