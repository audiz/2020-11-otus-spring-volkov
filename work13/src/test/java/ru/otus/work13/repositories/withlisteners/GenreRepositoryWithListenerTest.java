package ru.otus.work13.repositories.withlisteners;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work13.domain.Book;
import ru.otus.work13.domain.Genre;
import ru.otus.work13.events.CascadeOperationsMongoEventListener;
import ru.otus.work13.events.GenreCascadeDeleteEventsListener;
import ru.otus.work13.repositories.AbstractRepositoryTest;
import ru.otus.work13.repositories.BookRepository;
import ru.otus.work13.repositories.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DisplayName("GenreRepository при наличии listener-ов в контексте ")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({CascadeOperationsMongoEventListener.class, GenreCascadeDeleteEventsListener.class})
public class GenreRepositoryWithListenerTest extends AbstractRepositoryTest {

    public static final String NEW_NAME = "New Name";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен кидать RuntimeException во время удаления Genre с присутствующей книгой")
    @Test
    void shouldThrowRuntimeExceptionWhenRemove() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Genre> genre = genreRepository.findById(book.get().getAuthor().getId());
        assertThat(genre).isPresent();

        assertThatThrownBy(() -> genreRepository.delete(genre.get())).isInstanceOf(RuntimeException.class);
    }


    @DisplayName("при изменений Genre должен изменять его у книги")
    @Test
    void shouldChangeInBookWhenChanged() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isPresent();

        Optional<Genre> genre = genreRepository.findById(book.get().getAuthor().getId());
        assertThat(genre).isPresent();
        genre.get().setName(NEW_NAME);
        genreRepository.save(genre.get());
        assertThat(bookRepository.findById(1L)).isNotEmpty().get().extracting(Book::getGenre).extracting(Genre::getName).isEqualTo(NEW_NAME);
    }

}