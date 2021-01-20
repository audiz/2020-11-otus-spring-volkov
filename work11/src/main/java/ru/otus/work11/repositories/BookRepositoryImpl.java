package ru.otus.work11.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.work11.domain.Book;

import javax.persistence.*;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> getFullById(Long id) {
        TypedQuery<Book> query = em.createQuery("select s from Book s where s.id = :id", Book.class);
        query.setParameter("id", id);

        EntityGraph<?> entityGraph = em.getEntityGraph("author-and-genre-entity-graph");
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());
    }
}
