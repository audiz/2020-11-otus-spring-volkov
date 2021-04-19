package ru.otus.work35.rest;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ru.otus.work35.domain.Author;
import ru.otus.work35.domain.Genre;
import ru.otus.work35.exceptions.AlreadyExistException;
import ru.otus.work35.exceptions.NotFoundException;
import ru.otus.work35.repositories.BookRepository;
import ru.otus.work35.repositories.GenreRepository;
import ru.otus.work35.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
@RestController
public class GenreController {
    private static final String GENRE_CONTROLLER = "GenreController";

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    @CircuitBreaker(name = GENRE_CONTROLLER, fallbackMethod = "getAllFallback")
    @Bulkhead(name = GENRE_CONTROLLER, type = Bulkhead.Type.THREADPOOL)
    @Retry(name = GENRE_CONTROLLER)
    @TimeLimiter(name = GENRE_CONTROLLER)
    @GetMapping("/api/genres")
    public CompletableFuture<CollectionModel<Genre>> all() {

        if(new Random().nextInt(3) == 2) {
            Try.run(() -> Thread.sleep(2100));
        }

        List<Genre> genres = genreRepository.findAll();
        return CompletableFuture.completedFuture(CollectionModel.of(genres));
    }

    @CircuitBreaker(name = GENRE_CONTROLLER)
    @Bulkhead(name = GENRE_CONTROLLER)
    @RateLimiter(name = GENRE_CONTROLLER)
    @PostMapping("/api/genres")
    public EntityModel<Genre> create(@RequestBody Genre genre) {
        if(genreRepository.isExistsByNameRegexIgnoreCase(genre.getName())) {
            throw new AlreadyExistException("Genre with name '"+ genre.getName() +"' already exists");
        }
        genre.setId(service.getSequenceNumber(Genre.SEQUENCE_NAME));
        genreRepository.save(genre);
        return EntityModel.of(genre);
    }

    @CircuitBreaker(name = GENRE_CONTROLLER)
    @Bulkhead(name = GENRE_CONTROLLER)
    @RateLimiter(name = GENRE_CONTROLLER)
    @PutMapping("/api/genres/{id}")
    public EntityModel<Genre> save(@RequestBody Genre genre, @PathVariable long id) {
        genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre not found"));
        genre.setId(id);
        genreRepository.save(genre);
        return EntityModel.of(genre);
    }

    @CircuitBreaker(name = GENRE_CONTROLLER)
    @Bulkhead(name = GENRE_CONTROLLER)
    @Retry(name = GENRE_CONTROLLER)
    @DeleteMapping("/api/genres/{id}")
    public void delete(@PathVariable("id") long id) {
        genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre not found"));
        if(bookRepository.isExistsByGenreId(id)){
            throw new AlreadyExistException("Genre linked for books");
        }
        genreRepository.deleteById(id);
    }

    private CompletableFuture<CollectionModel<Author>> getAllFallback(Exception ex) {
        return CompletableFuture.completedFuture(CollectionModel.of(CollectionModel.of(new ArrayList<>(0))));
    }
}
