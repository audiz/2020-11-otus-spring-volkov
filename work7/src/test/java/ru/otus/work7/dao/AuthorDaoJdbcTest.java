package ru.otus.work7.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.work7.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {
    private static final int EXPECTED_AUTHOR_COUNT = 3;
    @Autowired
    private AuthorDao authorDao;

    @DisplayName("возвращать ожидаемое количество авторов в БД")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        int actualAuthorsCount = authorDao.getAll().size();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHOR_COUNT);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor()  {
        var expectedAuthor = new Author(4, "Leo");
        authorDao.insert(expectedAuthor);
        var actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по id")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = new Author(1, "George Orwell");
        var actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        final var authorForDeleteId = 3;
        authorDao.deleteById(authorForDeleteId);
        assertThat(authorDao.getById(authorForDeleteId)).isNull();
    }

    @DisplayName("не удалять автора привязанного к книге")
    @Test
    void shouldNotDeleteAuthorById() {
        final var authorForDeleteId = 1;
        assertThatThrownBy(() -> authorDao.deleteById(authorForDeleteId)).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(authorDao.getById(authorForDeleteId)).isNotNull();
    }

    @DisplayName("редактировать автора")
    @Test
    void shouldEditAuthor() {
        var expectedAuthor = new Author(1, "Leo");
        authorDao.update(expectedAuthor);
        var actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

}