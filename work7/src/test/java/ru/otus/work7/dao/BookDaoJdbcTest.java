package ru.otus.work7.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Book;
import ru.otus.work7.domain.Genre;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {
    private static final int EXPECTED_BOOK_COUNT = 2;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private GenreDao genreDao;

    @DisplayName("возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBooksCount() {
        int actualBooksCount = bookDao.getAll().size();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook()  {
        Genre genre = genreDao.getById(1);
        assertThat(genre).isNotNull();
        Author author = authorDao.getById(2);
        assertThat(author).isNotNull();

        var expectedBook = new Book(3, "New book", genre, author);
        bookDao.insert(expectedBook);
        var actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("бросить исключение при попытке добавить книгу с существующим айди")
    @Test
    void shouldThrownExceptionOnInsert() {
        var book = bookDao.getById(1);
        assertThatThrownBy(() -> bookDao.insert(book)).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("возвращать ожидаемую книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        Genre genre = genreDao.getById(1);
        assertThat(genre).isNotNull();
        Author author = authorDao.getById(1);
        assertThat(author).isNotNull();

        var expectedBook = new Book(1, "Nineteen Eighty-Four", genre, author);
        var actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("удалять заданную книгу по его id")
    @Test
    void shouldCorrectDeleteBookById() {
        final var bookForDeleteId = 1;
        bookDao.deleteById(bookForDeleteId);
        assertThat(bookDao.getById(bookForDeleteId)).isNull();
    }

    @DisplayName("редактировать книгу")
    @Test
    void shouldEditBook() {
        Author author = authorDao.getById(2);
        assertThat(author).isNotNull();

        var expectedBook = bookDao.getById(1);
        expectedBook.setTitle("1984");
        expectedBook.setAuthor(author);

        bookDao.update(expectedBook);
        var actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("не редактировать не существующую книгу")
    @Test
    void shouldNotEditBook() {
        var expectedBook = bookDao.getById(1);
        var newBook = new Book(3, "New", expectedBook.getGenre(), expectedBook.getAuthor());
        assertThat(bookDao.update(newBook)).isEqualTo(0);
    }

}