package ru.otus.work35.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.work35.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    @DeleteQuery(value="{ 'book.$id' : :#{#bookId} }")
    void deleteAllByBookId(@Param("bookId") long bookId);

    @Query(value="{ 'book.$id' : :#{#bookId} }")
    List<Comment> findAllByBookId(@Param("bookId") long bookId);

    @ExistsQuery(value="{ 'book.$id' : :#{#bookId}, 'comment' : {$regex : /^:#{#comment}$/, $options: 'i'} }")
    boolean isExistsByBookAndCommentRegexIgnoreCase(@Param("bookId") long bookId, @Param("comment") String comment);
}