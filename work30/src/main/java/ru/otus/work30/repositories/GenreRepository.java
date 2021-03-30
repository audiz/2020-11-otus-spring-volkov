package ru.otus.work30.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work30.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {

}