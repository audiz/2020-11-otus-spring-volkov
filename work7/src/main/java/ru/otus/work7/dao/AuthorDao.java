package ru.otus.work7.dao;

import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Genre;

import java.util.List;

public interface AuthorDao {
    List<Author> getAll();
    void insert(Author author);
    Author getById(long id);
    void update(Author perauthorson);
    int deleteById(long id);
}
