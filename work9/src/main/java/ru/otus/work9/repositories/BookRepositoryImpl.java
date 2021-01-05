package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.work9.domain.Book;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select s from Book s join fetch s.author join fetch s.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public void insert(Book book) {
         em.persist(book);
    }

    @Override
    public Book update(Book book) {
        return em.merge(book);
    }

    @Override
    public Optional<Book> getFullById(long id) {
        TypedQuery<Book> query = em.createQuery("select s from Book s where s.id = :id", Book.class);
        query.setParameter("id", id);

        EntityGraph<?> entityGraph = em.getEntityGraph("entity-graph");
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public void delete(Book book) {
        //Query query = em.createQuery("delete from Book s where s.id = :id");
        //query.setParameter("id", book.getId());
        //query.executeUpdate();
        em.remove(book);
    }

    @Override
    public Long getCountByGenreId(long id) {
        TypedQuery<Long> query = em.createQuery("select count(s) from Book s where s.genre.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Long getCountByAuthorId(long id) {
        TypedQuery<Long> query = em.createQuery("select count(s) from Book s where s.author.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
