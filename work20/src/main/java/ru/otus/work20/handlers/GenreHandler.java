package ru.otus.work20.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.ApiError;
import ru.otus.work20.domain.Genre;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@RequiredArgsConstructor
@Component
public class GenreHandler {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> get(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Genre> genre = genreRepository.findById(id);
        return genre
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(genre, Genre.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(genreRepository.findAll(), Genre.class));
    }

    public Mono<ServerResponse> put(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Genre> genres = request.bodyToMono(Genre.class);
        return genreRepository
                .findById(id)
                .flatMap(
                        old ->
                                ok().contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                fromPublisher(
                                                        genres
                                                                .map(p -> new Genre(id, p.getName()))
                                                                .flatMap(p -> genreRepository.save(p)),
                                                        Genre.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> post(ServerRequest request) {

        final Mono<Genre> genres = request.bodyToMono(Genre.class);

        return genres.flatMap(genre -> Mono.zip(genreRepository.isExistsByNameRegexIgnoreCase(genre.getName()), Mono.just(genre)))
                .flatMap(zip  -> {
                       if(zip.getT1()) {
                            return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Genre already exists"));
                        }
                        else {
                           return Mono.just(zip.getT2())
                                    .zipWith(sequenceGeneratorService.getReactiveSequenceNumber(Genre.SEQUENCE_NAME))
                                    .map(objects -> new Genre(objects.getT2(), objects.getT1().getName()))
                                    .flatMap(genreRepository::save)
                                    .flatMap(genre -> ServerResponse.created(URI.create("/api/genres/" + genre.getId())).syncBody(genre))
                                    .switchIfEmpty(genres.flatMap(genre -> ServerResponse.badRequest().syncBody("Unknown error for " + genre.getId())));
                        }
                    }
                );
    }


    public Mono<ServerResponse> delete(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));

        return bookRepository.isExistsByGenreId(id).flatMap(aBoolean -> {
            if(aBoolean) {
                return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Genre linked for books"));
            } else {
                return genreRepository
                        .findById(id)
                        .flatMap(p -> noContent().build(genreRepository.delete(p)))
                        .switchIfEmpty(notFound().build());
            }
        });
    }

}