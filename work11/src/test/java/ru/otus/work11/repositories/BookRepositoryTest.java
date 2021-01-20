package ru.otus.work11.repositories;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.work11.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@DisplayName("Репозиторий на основе Spring Data для работы с книгами ")
@DataJpaTest
@AutoConfigureTestDatabase(connection= EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать полную информацию о нужной книге по ее id")
    @Test
    void shouldFindExpectedBookById() {
        val optionalActualBook = bookRepository.getFullById(1L);
        val expectedBook = em.find(Book.class, 1L);
        assertThat(optionalActualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

}
