package ru.otus.work17.rest;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.work17.domain.Author;
import ru.otus.work17.domain.Book;
import ru.otus.work17.domain.Genre;
import ru.otus.work17.repositories.AuthorRepository;
import ru.otus.work17.repositories.BookRepository;
import ru.otus.work17.repositories.CommentRepository;
import ru.otus.work17.repositories.GenreRepository;
import ru.otus.work17.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookController тест REST API книги")
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BookController.class, excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class BookControllerTests {
    public static final String TEST_BOOK_NAME = "Test Book String";
    public static final String TEST_NEW_BOOK_NAME = "NEW Book String";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private SequenceGeneratorService service;
    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("должен находить книгу методом GET по URL /api/books")
    @Test
    public void testGetApi() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, TEST_BOOK_NAME, new Genre(1, "Genre"), new Author(1, "Author"), new ArrayList<>()));

        given(bookRepository.findAll()).willReturn(books);
        MvcResult result =  mvc.perform(get("/api/books")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(TEST_BOOK_NAME);
    }

    @DisplayName("должен добавлять книгу методом POST по URL /api/books")
    @Test
    public void testPostApi() throws Exception {
        given(authorRepository.findById(1L)).willReturn(Optional.of(new Author(1, "George Orwell")));
        given(genreRepository.findById(2L)).willReturn(Optional.of(new Genre(2, "Science Fiction")));

        String insertJson = "{\"id\":0,\"title\":\"" + TEST_BOOK_NAME + "\",\"genre\":{\"id\":2,\"name\":\"Science Fiction\"}," +
                "\"author\":{\"id\":1,\"name\":\"George Orwell\"},\"commentsSize\":0}";
        MvcResult result =  mvc.perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(bookRepository, times(1)).save(any());

        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(argument.capture());
        assertEquals(TEST_BOOK_NAME, argument.getValue().getTitle());
    }

    @DisplayName("должен изменять книгу методом PUT по URL /api/books/:id")
    @Test
    public void testPutApi() throws Exception {
        val author = new Author(1, "George Orwell");
        val genre = new Genre(2, "Science Fiction");
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(genreRepository.findById(2L)).willReturn(Optional.of(genre));

        val book = new Book(1, TEST_BOOK_NAME, genre, author, new ArrayList<>());
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        String insertJson = "{\"id\":1,\"title\":\"" + TEST_NEW_BOOK_NAME + "\",\"genre\":{\"id\":2,\"name\":\"Science Fiction\"}," +
                "\"author\":{\"id\":1,\"name\":\"George Orwell\"},\"commentsSize\":0}";
        MvcResult result =  mvc.perform(
                put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(bookRepository, times(1)).save(any());

        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(argument.capture());
        assertEquals(TEST_NEW_BOOK_NAME, argument.getValue().getTitle());
    }

    @DisplayName("должен удалять книгу методом DELETE по URL /api/books/:id")
    @Test
    public void testDeleteApi() throws Exception {
        val book = new Book(1, TEST_BOOK_NAME, new Genre(2, "Science Fiction"), new Author(1, "George Orwell"), new ArrayList<>());
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        mvc.perform(delete("/api/books/1")).andExpect(status().isOk());
        verify(bookRepository, times(1)).deleteById(any());
    }

    @DisplayName("должен возвращать ошибку если удалять не существующую книгу методом DELETE по URL /api/books/:id")
    @Test
    public void testDeleteErrorApi() throws Exception {
        mvc.perform(delete("/api/books/1")).andExpect(status().is4xxClientError());
    }
}
