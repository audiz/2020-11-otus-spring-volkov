package ru.otus.work7.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Book;
import ru.otus.work7.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    private static final int EXPECTED_GENRE_COUNT = 2;
    @Autowired
    private GenreDao genreDao;

    @DisplayName("возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenresCount() {
        int actualGenresCount = genreDao.getAll().size();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRE_COUNT);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre()  {
        var expectedGenre = new Genre(3, "New genre");
        genreDao.insert(expectedGenre);
        var actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по id")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = new Genre(1, "Science Fiction");
        var actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {
        final var genreForDeleteId = 2;
        genreDao.deleteById(genreForDeleteId);
        assertThat(genreDao.getById(genreForDeleteId)).isNull();
    }

    @DisplayName("не удалять жанр привязанный к книге")
    @Test
    void shouldNotDeleteGenreById() {
        final var genreForDeleteId = 1;
        assertThatThrownBy(() -> genreDao.deleteById(genreForDeleteId)).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(genreDao.getById(genreForDeleteId)).isNotNull();
    }

    @DisplayName("редактировать жанр")
    @Test
    void shouldEditGenre() {
        var expectedGenre = new Genre(1, "Some Genre");
        genreDao.update(expectedGenre);
        var actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

}