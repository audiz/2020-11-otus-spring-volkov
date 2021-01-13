/*
package ru.otus.work11.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.work11.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(AuthorRepositoryImpl.class)
public class AuthorRepositoryTest {

    private static final String AUTHOR_NAME = "new name";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен корректно сохранять всю информацию о авторе")
    @Test
    void shouldSaveAllAuthorInfo() {

        val author = new Author(0, AUTHOR_NAME);
        authorRepository.insert(author);
        assertThat(author.getId()).isGreaterThan(0);

        val actualAuthor = em.find(Author.class, author.getId());
        assertThat(actualAuthor).isNotNull().matches(s -> !s.getName().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужном авторе по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        val optionalActualAuthor = authorRepository.findById(1L);
        val expectedAuthor = em.find(Author.class, 1L);
        assertThat(optionalActualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName(" должен изменять имя заданного автора по его id")
    @Test
    void shouldUpdateAuthorById() {
        val firstAuthor = em.find(Author.class, 1L);
        String oldName = firstAuthor.getName();
        em.detach(firstAuthor);
        firstAuthor.setName(AUTHOR_NAME);
        authorRepository.update(firstAuthor);
        val updatedAuthor = em.find(Author.class, 1L);

        assertThat(updatedAuthor.getName()).isNotEqualTo(oldName).isEqualTo(AUTHOR_NAME);
    }

    @DisplayName(" должен удалять заданного автора по его id")
    @Test
    void shouldDeleteAuthorById() {
        val deleteAuthor = em.find(Author.class, 3L);
        assertThat(deleteAuthor).isNotNull();

        authorRepository.delete(deleteAuthor);
        val deletedAuthor = em.find(Author.class, 3L);

        assertThat(deletedAuthor).isNull();
    }

}
*/
