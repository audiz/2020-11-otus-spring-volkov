package ru.otus.work9.repositories;

import ru.otus.work9.domain.Author;
import ru.otus.work9.domain.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> getAll();
    void insert(Author author);
    Author update(Author author);
    Optional<Author> findById(long id);
    void delete(Author author);
}
