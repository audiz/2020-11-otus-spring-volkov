package ru.otus.work35.rest;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import ru.otus.work35.domain.Author;
import ru.otus.work35.domain.Summary;
import ru.otus.work35.exceptions.AlreadyExistException;
import ru.otus.work35.exceptions.NotFoundException;
import ru.otus.work35.repositories.AuthorRepository;
import ru.otus.work35.repositories.BookRepository;
import ru.otus.work35.service.SequenceGeneratorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
@RestController
public class AuthorController {
    private static final String AUTHOR_CONTROLLER = "AuthorController";

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    @CircuitBreaker(name = AUTHOR_CONTROLLER, fallbackMethod = "getAllFallback")
    @Bulkhead(name = AUTHOR_CONTROLLER, type = Bulkhead.Type.THREADPOOL)
    @Retry(name = AUTHOR_CONTROLLER)
    @TimeLimiter(name = AUTHOR_CONTROLLER)
    @GetMapping("/api/authors")
    public CompletableFuture<CollectionModel<Author>> all() {

        if(new Random().nextInt(3) == 2) {
            Try.run(() -> Thread.sleep(2100));
        }

        List<Author> authors = authorRepository.findAll();
        return CompletableFuture.completedFuture(CollectionModel.of(authors));
    }

    @CircuitBreaker(name = AUTHOR_CONTROLLER)
    @Bulkhead(name = AUTHOR_CONTROLLER)
    @RateLimiter(name = AUTHOR_CONTROLLER)
    @PostMapping("/api/authors")
    public EntityModel<Author> create(@RequestBody Author author) {
        if(authorRepository.isExistsByNameRegexIgnoreCase(author.getName())) {
            throw new AlreadyExistException("Author with name '"+ author.getName() +"' already exists");
        }
        author.setId(service.getSequenceNumber(Author.SEQUENCE_NAME));
        authorRepository.save(author);
        return EntityModel.of(author);
    }

    @CircuitBreaker(name = AUTHOR_CONTROLLER)
    @Bulkhead(name = AUTHOR_CONTROLLER)
    @RateLimiter(name = AUTHOR_CONTROLLER)
    @PutMapping("/api/authors/{id}")
    public EntityModel<Author> save(@RequestBody Author author, @PathVariable long id) {
        authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        author.setId(id);
        authorRepository.save(author);
        return EntityModel.of(author);
    }

    @CircuitBreaker(name = AUTHOR_CONTROLLER)
    @Bulkhead(name = AUTHOR_CONTROLLER)
    @Retry(name = AUTHOR_CONTROLLER)
    @DeleteMapping("/api/authors/{id}")
    public void delete(@PathVariable("id") long id) {
        authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        if(bookRepository.isExistsByAuthorId(id)){
            throw new AlreadyExistException("Author linked for books");
        }
        authorRepository.deleteById(id);
    }

    private CompletableFuture<CollectionModel<Author>> getAllFallback(Exception ex) {
        return CompletableFuture.completedFuture(CollectionModel.of(CollectionModel.of(new ArrayList<>(0))));
    }
}
