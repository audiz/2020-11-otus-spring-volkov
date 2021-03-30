package ru.otus.work30.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.work30.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, Long> {

}