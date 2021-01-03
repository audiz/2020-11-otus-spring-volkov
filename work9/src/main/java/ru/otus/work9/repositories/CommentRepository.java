package ru.otus.work9.repositories;

import ru.otus.work9.domain.Comment;

import java.util.Optional;

public interface CommentRepository {
    void insert(Comment comment);
    Comment update(Comment comment);
    Optional<Comment> findById(long id);
    void delete(Comment comment);
}
