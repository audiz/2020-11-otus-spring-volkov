package ru.otus.work13.repositories.plain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Author;
import ru.otus.work13.domain.Book;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.AuthorRepository;
import ru.otus.work13.repositories.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("AuthorRepository при отсутствии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorRepositoryWithoutListenerTest  extends AbstractRepositoryTest {

    public static final String NEW_NAME = "New Name";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("при удалении Author не должен удалять его из книги")
    @Test
    void shouldNotRemoveFromBookWhenRemoved() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Author> author = authorRepository.findById(book.get().getAuthor().getId());
        assertThat(author).isPresent();

        authorRepository.delete(author.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getAuthor).isNotNull();
    }

    @DisplayName("при изменений Author не должен изменять его у книги")
    @Test
    void shouldNotChangeInBookWhenChanged() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Author> author = authorRepository.findById(book.get().getAuthor().getId());
        assertThat(author).isPresent();
        var oldName = author.get().getName();
        author.get().setName(NEW_NAME);
        authorRepository.save(author.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getAuthor).extracting(Author::getName).isEqualTo(oldName);
    }
}