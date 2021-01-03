package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.work9.domain.Author;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select s from Author s", Author.class);
        return query.getResultList();
    }

    @Override
    public void insert(Author author) {
         em.persist(author);
    }

    @Override
    public Author update(Author author) {
        return em.merge(author);
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public void delete(Author author) {
        em.remove(author);
    }
}
