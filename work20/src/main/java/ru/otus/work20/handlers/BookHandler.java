package ru.otus.work20.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.ApiError;
import ru.otus.work20.domain.Book;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.net.URI;
import java.util.ArrayList;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@RequiredArgsConstructor
@Component
public class BookHandler {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> get(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Book> book = bookRepository.findById(id);
        return book
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(book, Book.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(bookRepository.findAll(), Book.class));
    }

    public Mono<ServerResponse> put(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Book> books = request.bodyToMono(Book.class);

        return books.flatMap(book -> Mono.zip(Mono.just(book),
                authorRepository.findById(book.getAuthor().getId()),
                genreRepository.findById(book.getGenre().getId())))
                .flatMap(zip  -> bookRepository
                            .findById(id)
                            .map(old -> {
                                old.setTitle(zip.getT1().getTitle());
                                old.setGenre(zip.getT3());
                                old.setAuthor(zip.getT2());
                                return old;
                            })
                            .flatMap(bookRepository::save)
                            .flatMap(book -> ServerResponse.created(URI.create("/api/books/" + book.getId())).syncBody(book))
                            .switchIfEmpty(notFound().build()));
    }

    public Mono<ServerResponse> post(ServerRequest request) {

        final Mono<Book> books = request.bodyToMono(Book.class);

        return books.flatMap(book -> Mono.zip(
                bookRepository.isExistsByTitleRegexIgnoreCase(book.getTitle()),
                Mono.just(book),
                authorRepository.findById(book.getAuthor().getId()),
                genreRepository.findById(book.getGenre().getId())))
                .flatMap(zip  -> {
                       if(zip.getT1()) {
                            return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Book already exists"));
                       }
                       else {
                           return Mono.just(zip.getT2())
                                    .zipWith(sequenceGeneratorService.getReactiveSequenceNumber(Book.SEQUENCE_NAME))
                                    .map(objects -> new Book(objects.getT2(), objects.getT1().getTitle(), zip.getT4(), zip.getT3(), new ArrayList<>()))
                                    .flatMap(bookRepository::save)
                                    .flatMap(book -> ServerResponse.created(URI.create("/api/books/" + book.getId())).syncBody(book))
                                    .switchIfEmpty(books.flatMap(book -> ServerResponse.badRequest().syncBody("Unknown error for " + book.getId())));
                       }
                    }
                );
    }


    public Mono<ServerResponse> delete(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        return bookRepository
                .findById(id)
                .flatMap(p -> noContent().build(bookRepository.delete(p)))
                .switchIfEmpty(notFound().build());
    }

}