package ru.otus.work9.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Author;
import ru.otus.work9.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select s from Genre s", Genre.class);
        return query.getResultList();
    }

    @Override
    public void insert(Genre genre) {
         em.persist(genre);
    }

    @Override
    public Genre update(Genre genre) {
        return em.merge(genre);
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public void delete(Genre genre) {
        em.remove(genre);
    }
}
