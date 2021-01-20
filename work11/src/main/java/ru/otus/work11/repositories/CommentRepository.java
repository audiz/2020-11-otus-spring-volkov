package ru.otus.work11.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.work11.domain.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAll();
}