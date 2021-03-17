package ru.otus.work26.batch;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work26.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, Long> {
}