package ru.otus.work7.dao;

import ru.otus.work7.domain.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();
    void insert(Genre genre);
    Genre getById(long id);
    void update(Genre genre);
    int deleteById(long id);
}
