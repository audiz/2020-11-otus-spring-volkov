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
import ru.otus.work20.domain.Summary;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.CommentRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.net.URI;
import java.util.ArrayList;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@RequiredArgsConstructor
@Component
public class SummaryHandler {

    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> all(ServerRequest request) {
        return Mono.zip(bookRepository.count(), authorRepository.count(), genreRepository.count(),  commentRepository.count())
                .map(objects -> new Summary(objects.getT1(), objects.getT2(), objects.getT3(), objects.getT4()))
                .flatMap(summary -> ServerResponse.ok().syncBody(summary));
    }

}