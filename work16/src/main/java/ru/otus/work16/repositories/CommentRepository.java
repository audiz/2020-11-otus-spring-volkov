package ru.otus.work16.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.work16.domain.Comment;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    @DeleteQuery(value="{ 'book.$id' : :#{#bookId} }")
    void deleteAllByBookId(@Param("bookId") long bookId);
}