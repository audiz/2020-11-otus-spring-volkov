package ru.otus.work13.repositories.plain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Author;
import ru.otus.work13.domain.Book;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("BookRepository при отсутствии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookRepositoryWithoutListenerTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;


    @DisplayName("проверить наличие Book по идентификатору автора")
    @Test
    void shouldCheckIsExistByAuthorId() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        assertThat(bookRepository.isExistsByAuthorId(book.get().getAuthor().getId())).isTrue();
    }

    @DisplayName("проверить наличие Book по идентификатору жанра")
    @Test
    void shouldCheckIsExistByGenreId() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        assertThat(bookRepository.isExistsByGenreId(book.get().getGenre().getId())).isTrue();
    }
}