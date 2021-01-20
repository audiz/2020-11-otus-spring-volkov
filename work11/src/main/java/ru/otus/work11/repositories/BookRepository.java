package ru.otus.work11.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.work11.domain.Book;

import java.util.List;


public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findAll();
}