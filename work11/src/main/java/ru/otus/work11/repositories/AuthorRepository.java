package ru.otus.work11.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.work11.domain.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAll();
}