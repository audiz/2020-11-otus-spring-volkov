package ru.otus.work9.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.work9.domain.Author;
import ru.otus.work9.domain.Book;
import ru.otus.work9.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({BookRepositoryImpl.class})
public class BookRepositoryTest {

    private static final String TITLE = "new title";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен корректно сохранять всю информацию о книге")
    @Test
    void shouldSaveAllBookInfo() {
        Genre genre = em.find(Genre.class, 1L);
        assertThat(genre).isNotNull();
        Author author = em.find(Author.class, 1L);
        assertThat(author).isNotNull();

        val book = new Book(0, TITLE, genre, author, null);
        bookRepository.insert(book);
        assertThat(book.getId()).isGreaterThan(0);

        val actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужной книге по ее id")
    @Test
    void shouldFindExpectedBookById() {
        val optionalActualBook = bookRepository.findById(1L);
        val expectedBook = em.find(Book.class, 1L);
        assertThat(optionalActualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName(" должен изменять имя заданной книги по ее id")
    @Test
    void shouldUpdateBookNameById() {
        val firstBook = em.find(Book.class, 1L);
        String oldComment = firstBook.getTitle();
        em.detach(firstBook);
        firstBook.setTitle(TITLE);
        bookRepository.update(firstBook);
        val updatedBook = em.find(Book.class, 1L);

        assertThat(updatedBook.getTitle()).isNotEqualTo(oldComment).isEqualTo(TITLE);
    }

    @DisplayName(" должен удалять заданную книгу по ее id")
    @Test
    void shouldDeleteBookById() {
        val deleteBook = em.find(Book.class, 2L);
        assertThat(deleteBook).isNotNull();

        bookRepository.delete(deleteBook);
        val deletedBook = em.find(Book.class, 2L);

        assertThat(deletedBook).isNull();
    }

}
