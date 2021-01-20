package ru.otus.work11.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.work11.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    List<Genre> findAll();
}