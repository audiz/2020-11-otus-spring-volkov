package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> getAllByBookId(long id) {
        TypedQuery<Comment> query = em.createQuery("select s from Comment s left join s.bookId b where b.id = :id ", Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

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
    public int deleteById(long id) {
        Query query = em.createQuery("delete from Comment s where s.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }
}
