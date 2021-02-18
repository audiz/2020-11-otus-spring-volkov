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
import ru.otus.work20.domain.Comment;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.CommentRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@RequiredArgsConstructor
@Component
public class CommentHandler {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> get(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        final Mono<Comment> comment = commentRepository.findById(id);
        return comment
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(comment, Comment.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        final Long bookId = Long.parseLong(request.pathVariable("bookId"));
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(commentRepository.findAllByBookId(bookId), Comment.class));
    }

    public Mono<ServerResponse> post(ServerRequest request) {
        final Long bookId = Long.parseLong(request.pathVariable("bookId"));

        final Mono<Comment> comments = request.bodyToMono(Comment.class);

        return comments.flatMap(comment -> Mono.zip(
                commentRepository.isExistsByBookAndCommentRegexIgnoreCase(bookId, comment.getComment()),
                Mono.just(comment),
                bookRepository.findById(bookId)))
                .flatMap(zip  -> {
                       if(zip.getT1()) {
                            return ServerResponse.badRequest().syncBody(new ApiError(HttpStatus.BAD_REQUEST,"Author already exists"));
                        }
                        else {
                           return Mono.just(zip.getT2())
                                    .zipWith(sequenceGeneratorService.getReactiveSequenceNumber(Comment.SEQUENCE_NAME))
                                    .map(objects -> new Comment(objects.getT2(), objects.getT1().getComment(), zip.getT3()))
                                    .flatMap(commentRepository::save)
                                    .flatMap(author -> ServerResponse.ok().syncBody(author))
                                    .switchIfEmpty(comments.flatMap(comment -> ServerResponse.badRequest().syncBody("Unknown error for " + comment.getId())));
                        }
                    }
                );
    }


    public Mono<ServerResponse> delete(ServerRequest request) {
        final Long id = Long.parseLong(request.pathVariable("id"));
        return commentRepository
                .findById(id)
                .flatMap(p -> noContent().build(commentRepository.delete(p)))
                .switchIfEmpty(notFound().build());
    }

}