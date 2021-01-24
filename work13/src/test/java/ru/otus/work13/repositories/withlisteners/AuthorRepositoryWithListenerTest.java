package ru.otus.work13.repositories.withlisteners;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Author;
import ru.otus.work13.domain.Book;
import ru.otus.work13.events.AuthorCascadeDeleteEventsListener;
import ru.otus.work13.events.CascadeOperationsMongoEventListener;
import ru.otus.work13.events.GenreCascadeDeleteEventsListener;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.AuthorRepository;
import ru.otus.work13.repositories.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DisplayName("AuthorRepository при наличии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({CascadeOperationsMongoEventListener.class, AuthorCascadeDeleteEventsListener.class})
public class AuthorRepositoryWithListenerTest extends AbstractRepositoryTest {

    public static final String NEW_NAME = "New Name";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен кидать RuntimeException во время удаления Author с присутствующей книгой")
    @Test
    void shouldThrowRuntimeExceptionWhenRemove() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Author> author = authorRepository.findById(book.get().getAuthor().getId());
        assertThat(author).isPresent();

        assertThatThrownBy(() -> authorRepository.delete(author.get())).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("при изменений Author должен изменять его у книги")
    @Test
    void shouldChangeInBookWhenChanged() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Author> author = authorRepository.findById(book.get().getAuthor().getId());
        assertThat(author).isPresent();
        author.get().setName(NEW_NAME);
        authorRepository.save(author.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getAuthor).extracting(Author::getName).isEqualTo(NEW_NAME);
    }
}