package ru.otus.work13.repositories.plain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Book;
import ru.otus.work13.domain.Comment;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.BookRepository;
import ru.otus.work13.repositories.CommentRepository;
import ru.otus.work13.service.SequenceGeneratorService;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("CommentRepository при отсутствии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryWithoutListenerTest extends AbstractRepositoryTest {

    public static final String NEW_COMMENT = "New comment";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SequenceGeneratorService service;

    @DisplayName("при удалении Comment должен удалять его из книги")
    @Test
    void shouldNotRemoveFromBookWhenRemoved() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();
        int commentSize = book.get().getComments().size();

        Optional<Comment> comment = commentRepository.findById(book.get().getComments().get(0).getId());
        assertThat(comment).isPresent();

        commentRepository.deleteById(comment.get().getId());
        commentSize--;

        assertThat(commentRepository.findById(comment.get().getId())).isEmpty();

        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getComments).asList().hasSize(commentSize);
    }

    @DisplayName("при добавлении Comment не должен добавлять его в книгу")
    @Test
    void shouldNotAddInBookWhenCreated() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();
        int commentSize = book.get().getComments().size();

        Comment comment = new Comment(service.getSequenceNumber(Comment.SEQUENCE_NAME), "New comment",  book.get());

        commentRepository.save(comment);
        assertThat(commentRepository.findById(comment.getId())).isNotEmpty();

        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getComments).asList().hasSize(commentSize);
    }

    @DisplayName("должен удалять все Comment к книге по ее айди")
    @Test
    void shouldDeleteAllCommentByBookId() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();
        Iterable<Long> commentsIds = book.get().getComments().stream().map(Comment::getId).collect(Collectors.toList());

        commentRepository.deleteAllByBookId(book.get().getId());
        //assertThat(commentRepository.findById(book.get().getComments().get(0).getId())).isEmpty();

        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getComments).asList().isEmpty();
        assertThat(commentRepository.findAllById(commentsIds)).isEmpty();
    }


    @DisplayName("не должен удалять все Comment к книге если ее удалят")
    @Test
    void shouldNotDeleteAllCommentWhenBookRemoved() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();
        Iterable<Long> commentsIds = book.get().getComments().stream().map(Comment::getId).collect(Collectors.toList());

        bookRepository.delete(book.get());
        assertThat(bookRepository.findById(book.get().getId())).isEmpty();

        assertThat(commentRepository.findAllById(commentsIds)).isNotEmpty();
    }

}