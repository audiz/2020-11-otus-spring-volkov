package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.work9.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    public Comment update(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
