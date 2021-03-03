package ru.otus.work20.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.Author;
import ru.otus.work20.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, Long> {
    @ExistsQuery(value="{ 'genre._id' : :#{#genreId} }")
    Mono<Boolean> isExistsByGenreId(@Param("genreId")  long genreId);
    @ExistsQuery(value="{ 'author._id' : :#{#authorId} }")
    Mono<Boolean> isExistsByAuthorId(@Param("authorId")  long authorId);
    @ExistsQuery(value="{ 'title' : {$regex : /^:#{#title}$/, $options: 'i'} }")
    Mono<Boolean> isExistsByTitleRegexIgnoreCase(@Param("title") String title);
}