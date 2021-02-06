package ru.otus.work16.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work16.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, Long> {
}