package ru.otus.work13.repositories.withlisteners;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Book;
import ru.otus.work13.domain.Comment;
import ru.otus.work13.events.BookCascadeDeleteEventsListener;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.BookRepository;
import ru.otus.work13.repositories.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("BookRepository при наличии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(BookCascadeDeleteEventsListener.class)
public class BookRepositoryWithListenerTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("при удалении Book должен удалять все Comment")
    @Test
    void shouldRemoveCommentsWhenBookRemoved() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Comment> comment = commentRepository.findById(book.get().getComments().get(0).getId());
        assertThat(comment).isPresent();

        bookRepository.deleteById(book.get().getId());

        assertThat(bookRepository.findById(1L)).isEmpty();
        assertThat(commentRepository.findById(comment.get().getId())).isEmpty();
    }
}