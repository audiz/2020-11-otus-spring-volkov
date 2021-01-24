package ru.otus.work13.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work13.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, Long> {
}