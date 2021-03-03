package ru.otus.work20.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.ApiError;
import ru.otus.work20.domain.Author;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@RequiredArgsConstructor
@Component
public class AuthorHandler {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> get(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Author> author = authorRepository.findById(id);
        return author
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(author, Author.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(authorRepository.findAll(), Author.class));
    }

    public Mono<ServerResponse> put(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Author> authors = request.bodyToMono(Author.class);
        return authorRepository
                .findById(id)
                .flatMap(
                        old ->
                                ok().contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                fromPublisher(
                                                        authors
                                                                .map(p -> new Author(id, p.getName()))
                                                                .flatMap(authorRepository::save),
                                                        Author.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> post(ServerRequest request) {

        final Mono<Author> authors = request.bodyToMono(Author.class);

        return authors.flatMap(author -> Mono.zip(authorRepository.isExistsByNameRegexIgnoreCase(author.getName()), Mono.just(author)))
                .flatMap(zip  -> {
                       if(zip.getT1()) {
                            return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Author already exists"));
                        }
                        else {
                           return Mono.just(zip.getT2())
                                    .zipWith(sequenceGeneratorService.getReactiveSequenceNumber(Author.SEQUENCE_NAME))
                                    .map(objects -> new Author(objects.getT2(), objects.getT1().getName()))
                                    .flatMap(authorRepository::save)
                                    .flatMap(author -> ServerResponse.created(URI.create("/api/authors/" + author.getId())).syncBody(author))
                                    .switchIfEmpty(authors.flatMap(author -> ServerResponse.badRequest().syncBody("Unknown error for " + author.getId())));
                        }
                    }
                );
    }


    public Mono<ServerResponse> delete(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        return bookRepository.isExistsByAuthorId(id).flatMap(aBoolean -> {
            if(aBoolean) {
                return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Author linked for books"));
            } else {
                return authorRepository
                        .findById(id)
                        .flatMap(p -> noContent().build(authorRepository.delete(p)))
                        .switchIfEmpty(notFound().build());
            }
        });
    }

}