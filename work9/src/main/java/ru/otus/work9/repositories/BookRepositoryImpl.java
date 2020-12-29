package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    @Transactional
    @Override
    public void insert(Book book) {
         em.persist(book);
    }

    @Transactional
    @Override
    public Book update(Book book) {
        return em.merge(book);
    }

    @Override
    public Optional<Book> getFullById(long id) {
        TypedQuery<Book> query = em.createQuery("select s from Book s join fetch s.author join fetch s.genre where s.id = :id", Book.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Transactional
    @Override
    public int deleteById(long id) {
        Query query = em.createQuery("delete from Book s where s.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Override
    public int getCountGenreById(long id) {
        Query query =  em.createQuery("select count(s) from Book s inner join s.genre c where c.id = :id");
        query.setParameter("id", id);
        return query.getFirstResult();
    }

    @Override
    public int getCountAuthorById(long id) {
        Query query =  em.createQuery("select count(s) from Book s inner join s.author c where c.id = :id");
        query.setParameter("id", id);
        return query.getFirstResult();
    }
}
