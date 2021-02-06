package ru.otus.work16.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.work16.domain.Author;
import ru.otus.work16.domain.Book;
import ru.otus.work16.domain.Genre;
import ru.otus.work16.repositories.AuthorRepository;
import ru.otus.work16.repositories.BookRepository;
import ru.otus.work16.repositories.CommentRepository;
import ru.otus.work16.repositories.GenreRepository;
import ru.otus.work16.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookController тест GUI книги ")
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BookController.class, excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ControllerTests {
    public static final String TEST_BOOK = "Test Book String";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository repository;

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private SequenceGeneratorService service;
    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("должен находить книгу по урлу /books")
    @Test
    public void testGui() throws Exception {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1, TEST_BOOK, new Genre(1, "Genre"), new Author(1, "Author"), new ArrayList<>()));
        given(repository.findAll()).willReturn(bookList);
        MvcResult result =  mvc.perform(get("/books")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(TEST_BOOK);
    }
}
