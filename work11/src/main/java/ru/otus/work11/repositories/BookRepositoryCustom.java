package ru.otus.work11.repositories;

import ru.otus.work11.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> getFullById(Long id);
}
