package ru.otus.work16.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work16.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {
}