package ru.otus.work13.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work13.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {
}