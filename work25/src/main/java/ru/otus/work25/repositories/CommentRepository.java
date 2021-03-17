package ru.otus.work25.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.work25.domain.Comment;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
public interface CommentRepository extends MongoRepository<Comment, Long> {
    @DeleteQuery(value="{ 'book.$id' : :#{#bookId} }")
    void deleteAllByBookId(@Param("bookId") long bookId);

    @Query(value="{ 'book.$id' : :#{#bookId} }")
    List<Comment> findAllByBookId(@Param("bookId") long bookId);

    @ExistsQuery(value="{ 'book.$id' : :#{#bookId}, 'comment' : {$regex : /^:#{#comment}$/, $options: 'i'} }")
    boolean isExistsByBookAndCommentRegexIgnoreCase(@Param("bookId") long bookId, @Param("comment") String comment);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void deleteById(Long aLong);

    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @Override
    long count();
}