package ru.otus.work20.repositories;

import org.springframework.data.mongodb.repository.*;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, Long> {
    @DeleteQuery(value="{ 'book.$id' : :#{#bookId} }")
    Mono<Void> deleteAllByBookId(@Param("bookId") long bookId);

    @Query(value="{ 'book.$id' : :#{#bookId} }")
    Flux<Comment> findAllByBookId(@Param("bookId") long bookId);

    @ExistsQuery(value="{ 'book.$id' : :#{#bookId}, 'comment' : {$regex : /^:#{#comment}$/, $options: 'i'} }")
    Mono<Boolean> isExistsByBookAndCommentRegexIgnoreCase(@Param("bookId") long bookId, @Param("comment") String comment);
}