package ru.otus.work20.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, Long> {
    @ExistsQuery(value="{ 'name' : {$regex : /^:#{#name}$/, $options: 'i'} }")
    Mono<Boolean> isExistsByNameRegexIgnoreCase(@Param("name") String name);
}