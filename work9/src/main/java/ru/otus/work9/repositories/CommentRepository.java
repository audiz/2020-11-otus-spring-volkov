package ru.otus.work9.repositories;

import ru.otus.work9.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> getAllByBookId(long id);
    void insert(Comment comment);
    Comment update(Comment comment);
    Optional<Comment> findById(long id);
    int deleteById(long id);
}
