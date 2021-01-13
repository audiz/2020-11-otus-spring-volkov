/*
package ru.otus.work11.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.work11.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import(GenreRepositoryImpl.class)
public class GenreRepositoryTest {

    private static final String GENRE_NAME = "new name";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен корректно сохранять всю информацию о жанре")
    @Test
    void shouldSaveAllGenreInfo() {

        val genre = new Genre(0, GENRE_NAME);
        genreRepository.insert(genre);
        assertThat(genre.getId()).isGreaterThan(0);

        val actualGenre = em.find(Genre.class, genre.getId());
        assertThat(actualGenre).isNotNull().matches(s -> !s.getName().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужном жанре по его id")
    @Test
    void shouldFindExpectedGenreById() {
        val optionalActualGenre = genreRepository.findById(1L);
        val expectedGenre = em.find(Genre.class, 1L);
        assertThat(optionalActualGenre).isPresent().get().usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName(" должен изменять имя заданного жанра по его id")
    @Test
    void shouldUpdateGenreById() {
        val firstGenre = em.find(Genre.class, 1L);
        String oldName = firstGenre.getName();
        em.detach(firstGenre);
        firstGenre.setName(GENRE_NAME);
        genreRepository.update(firstGenre);
        val updatedGenre = em.find(Genre.class, 1L);

        assertThat(updatedGenre.getName()).isNotEqualTo(oldName).isEqualTo(GENRE_NAME);
    }

    @DisplayName(" должен удалять заданный жанр по его id")
    @Test
    void shouldDeleteGenreById() {
        val deleteGenre = em.find(Genre.class, 2L);
        assertThat(deleteGenre).isNotNull();

        genreRepository.delete(deleteGenre);
        val deletedGenre = em.find(Genre.class, 2L);

        assertThat(deletedGenre).isNull();
    }

}
*/
