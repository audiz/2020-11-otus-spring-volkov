package ru.otus.work35.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ru.otus.work35.domain.Comment;
import ru.otus.work35.exceptions.AlreadyExistException;
import ru.otus.work35.exceptions.NotFoundException;
import ru.otus.work35.repositories.BookRepository;
import ru.otus.work35.repositories.CommentRepository;
import ru.otus.work35.service.SequenceGeneratorService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Log4j2
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final SequenceGeneratorService service;


    @GetMapping("/api/books/{bookId}/comments")
    public CollectionModel<Comment> all(@PathVariable("bookId") long bookId) {
        bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        var comments = commentRepository.findAllByBookId(bookId);
        return  CollectionModel.of(comments);
    }

    @GetMapping(value = {"/api/comments/{commentId}", "/api/books/{bookId}/comments/{commentId}"})
    public EntityModel<Comment> get(@PathVariable(required = false, value = "bookId") Long bookId, @PathVariable("commentId") long commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        return EntityModel.of(comment);
    }

    @PostMapping("/api/books/{bookId}/comments")
    public EntityModel<Comment> create(@PathVariable("bookId") long bookId, @RequestBody Comment comment) {
        if(commentRepository.isExistsByBookAndCommentRegexIgnoreCase(bookId, comment.getComment())) {
            throw new AlreadyExistException("Comment already exists for this book");
        }
        var book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        comment.setId(service.getSequenceNumber(Comment.SEQUENCE_NAME));
        comment.setBook(book);
        commentRepository.save(comment);
        return  EntityModel.of(comment);
    }

    @PutMapping(value = {"/api/comments/{commentId}", "/api/books/{bookId}/comments/{commentId}"})
    public EntityModel<Comment> edit(@PathVariable("bookId") Long bookId, @PathVariable("commentId") long commentId, @RequestBody Comment comment) {
        var oldComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        oldComment.setComment(comment.getComment());
        commentRepository.save(oldComment);
        return EntityModel.of(oldComment);
    }

    @DeleteMapping(value = {"/api/comments/{commentId}", "/api/books/{bookId}/comments/{commentId}"})
    public void delete(@PathVariable(required = false, value = "bookId") Long bookId, @PathVariable("commentId") long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(commentId);
    }

}
