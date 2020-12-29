package ru.otus.work9.repositories;

import ru.otus.work9.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> getAll();
    void insert(Genre genre);
    Genre update(Genre genre);
    Optional<Genre> findById(long id);
    int deleteById(long id);
}
