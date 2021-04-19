package ru.otus.work35.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.work35.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, Long> {
    @ExistsQuery(value="{ 'name' : {$regex : /^:#{#name}$/, $options: 'i'} }")
    boolean isExistsByNameRegexIgnoreCase(@Param("name") String name);
}