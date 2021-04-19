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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import ru.otus.work35.domain.Author;
import ru.otus.work35.domain.Book;
import ru.otus.work35.domain.Genre;
import ru.otus.work35.exceptions.AlreadyExistException;
import ru.otus.work35.exceptions.NotFoundException;
import ru.otus.work35.repositories.AuthorRepository;
import ru.otus.work35.repositories.BookRepository;
import ru.otus.work35.repositories.GenreRepository;
import ru.otus.work35.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RequiredArgsConstructor
@RestController
public class BookController {
    private static final String BOOK_CONTROLLER = "BookController";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final SequenceGeneratorService service;

    @CircuitBreaker(name = BOOK_CONTROLLER, fallbackMethod = "getAllFallback")
    @Bulkhead(name = BOOK_CONTROLLER, type = Bulkhead.Type.THREADPOOL)
    @Retry(name = BOOK_CONTROLLER)
    @TimeLimiter(name = BOOK_CONTROLLER)
    @GetMapping("/api/books")
    public CompletableFuture<CollectionModel<EntityModel<Book>>> all() {

        if(new Random().nextInt(3) == 2) {
            Try.run(() -> Thread.sleep(2100));
        }

        var books = bookRepository.findAll();
        List<EntityModel<Book>> entityModels = new ArrayList<>();
        for (final Book book : books) {
            EntityModel<Book> model = EntityModel.of(book);
            model.add(linkTo(methodOn(CommentController.class).all(book.getId())).withRel("commentList"));
            entityModels.add(model);
        }
        CollectionModel<EntityModel<Book>> model = CollectionModel.of(entityModels);
        return CompletableFuture.completedFuture(model);
    }

    @CircuitBreaker(name = BOOK_CONTROLLER, fallbackMethod = "getBookFallback")
    @Bulkhead(name = BOOK_CONTROLLER)
    @Retry(name = BOOK_CONTROLLER)
    @GetMapping("/api/books/{id}")
    public EntityModel<Book> get(@PathVariable long id) {
        if(new Random().nextInt(3) == 2) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
        }

        var book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        var model = EntityModel.of(book);
        var link = linkTo(methodOn(CommentController.class).all(id)).withRel("commentList");
        model.add(link);
        return model;
    }

    @CircuitBreaker(name = BOOK_CONTROLLER)
    @Bulkhead(name = BOOK_CONTROLLER)
    @RateLimiter(name = BOOK_CONTROLLER)
    @PostMapping("/api/books")
    public EntityModel<Book> create(@RequestBody Book book) {
        if(bookRepository.isExistsByTitleRegexIgnoreCase(book.getTitle())) {
            throw new AlreadyExistException("Book with title '"+ book.getTitle() +"' already exists");
        }

        Optional.ofNullable(book.getAuthor()).orElseThrow(() -> new NotFoundException("Please select author"));
        Optional.ofNullable(book.getGenre()).orElseThrow(() -> new NotFoundException("Please select genre"));
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(() -> new NotFoundException("Author not found"));
        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Genre not found"));
        book.setAuthor(author);
        book.setGenre(genre);

        book.setComments(new ArrayList<>());
        book.setId(service.getSequenceNumber(Book.SEQUENCE_NAME));
        bookRepository.save(book);
        return EntityModel.of(book);
    }

    @CircuitBreaker(name = BOOK_CONTROLLER)
    @Bulkhead(name = BOOK_CONTROLLER)
    @RateLimiter(name = BOOK_CONTROLLER)
    @PutMapping("/api/books/{id}")
    public EntityModel<Book> save(@RequestBody Book book, @PathVariable long id) {
        var oldBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));

        Optional.ofNullable(book.getAuthor()).orElseThrow(() -> new NotFoundException("Please select author"));
        Optional.ofNullable(book.getGenre()).orElseThrow(() -> new NotFoundException("Please select genre"));
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(() -> new NotFoundException("Author not found"));
        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Genre not found"));
        book.setAuthor(author);
        book.setGenre(genre);

        book.setComments(oldBook.getComments());
        book.setId(id);
        bookRepository.save(book);
        return EntityModel.of(book);
    }

    @CircuitBreaker(name = BOOK_CONTROLLER)
    @Bulkhead(name = BOOK_CONTROLLER)
    @Retry(name = BOOK_CONTROLLER)
    @DeleteMapping("/api/books/{id}")
    public void delete(@PathVariable("id") long id) {
        bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        bookRepository.deleteById(id);
    }

    private CompletableFuture<CollectionModel<EntityModel<Book>>> getAllFallback(Exception ex) {
        return CompletableFuture.completedFuture(CollectionModel.of(new ArrayList<>(0)));
    }

    private EntityModel<Book> getBookFallback(Exception ex) {
        var book = new Book(0, "None", new Genre(), new Author(), new ArrayList<>());
        var model = EntityModel.of(book);
        var link = linkTo(methodOn(CommentController.class).all(0)).withRel("commentList");
        model.add(link);
        return model;
    }
}
