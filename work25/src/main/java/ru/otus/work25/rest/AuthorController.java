package ru.otus.work25.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ru.otus.work25.domain.Author;
import ru.otus.work25.exceptions.AlreadyExistException;
import ru.otus.work25.exceptions.NotFoundException;
import ru.otus.work25.repositories.AuthorRepository;
import ru.otus.work25.repositories.BookRepository;
import ru.otus.work25.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    @GetMapping("/api/authors")
    public CollectionModel<Author> all() {
        List<Author> authors = authorRepository.findAll();
        return CollectionModel.of(authors);
    }

    @PostMapping("/api/authors")
    public EntityModel<Author> create(@RequestBody Author author) {
        if(authorRepository.isExistsByNameRegexIgnoreCase(author.getName())) {
            throw new AlreadyExistException("Author with name '"+ author.getName() +"' already exists");
        }
        author.setId(service.getSequenceNumber(Author.SEQUENCE_NAME));
        authorRepository.save(author);
        return EntityModel.of(author);
    }

    @PutMapping("/api/authors/{id}")
    public EntityModel<Author> save(@RequestBody Author author, @PathVariable long id) {
        authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        author.setId(id);
        authorRepository.save(author);
        return EntityModel.of(author);
    }

    @DeleteMapping("/api/authors/{id}")
    public void delete(@PathVariable("id") long id) {
        authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        if(bookRepository.isExistsByAuthorId(id)){
            throw new AlreadyExistException("Author linked for books");
        }
        authorRepository.deleteById(id);
    }
}
