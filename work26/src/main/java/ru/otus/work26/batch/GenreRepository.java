package ru.otus.work26.batch;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work26.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {
}