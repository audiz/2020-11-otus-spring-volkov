package ru.otus.work13.repositories.plain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work13.domain.Author;
import ru.otus.work13.domain.Book;
import ru.otus.work13.domain.Genre;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.BookRepository;
import ru.otus.work13.repositories.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("GenreRepository при отсутствии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreRepositoryWithoutListenerTest extends AbstractRepositoryTest {

    public static final String NEW_NAME = "New Name";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("при удалении Genre не должен удалять его из книги")
    @Test
    void shouldNotRemoveFromBookWhenRemoved() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Genre> genre = genreRepository.findById(book.get().getAuthor().getId());
        assertThat(genre).isPresent();

        genreRepository.delete(genre.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getGenre).isNotNull();
    }


    @DisplayName("при изменений Genre не должен изменять его у книги")
    @Test
    void shouldNotChangeInBookWhenChanged() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Genre> genre = genreRepository.findById(book.get().getAuthor().getId());
        assertThat(genre).isPresent();
        var oldName = genre.get().getName();
        genre.get().setName(NEW_NAME);
        genreRepository.save(genre.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getGenre).extracting(Genre::getName).isEqualTo(oldName);
    }

}