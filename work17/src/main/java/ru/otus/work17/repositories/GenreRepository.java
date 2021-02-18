package ru.otus.work17.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.work17.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    @ExistsQuery(value="{ 'name' : {$regex : /^:#{#name}$/, $options: 'i'} }")
    boolean isExistsByNameRegexIgnoreCase(@Param("name") String name);
}