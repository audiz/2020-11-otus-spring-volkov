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

@DisplayName("AuthorController тест REST API автора")
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AuthorController.class, excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class AuthorControllerTests {
    public static final String TEST_AUTHOR_NAME = "Test Author String";
    public static final String TEST_NEW_AUTHOR_NAME = "NEW AUTHOR String";
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

    @DisplayName("должен находить автора методом GET по URL /api/authors")
    @Test
    public void testGetApi() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1, TEST_AUTHOR_NAME));

        given(authorRepository.findAll()).willReturn(authors);
        MvcResult result =  mvc.perform(get("/api/authors")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(TEST_AUTHOR_NAME);
    }

    @DisplayName("должен добавлять автора методом POST по URL /api/authors")
    @Test
    public void testPostApi() throws Exception {
        String insertJson = "{\"id\":0,\"name\":\"" + TEST_AUTHOR_NAME + "\"}";
        MvcResult result =  mvc.perform(
                post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(authorRepository, times(1)).save(any());

        ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(argument.capture());
        assertEquals(TEST_AUTHOR_NAME, argument.getValue().getName());
    }

    @DisplayName("должен изменять автора методом PUT по URL /api/authors/:id")
    @Test
    public void testPutApi() throws Exception {
        val author = new Author(1, TEST_AUTHOR_NAME);
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        String insertJson = "{\"id\":1,\"name\":\"" + TEST_NEW_AUTHOR_NAME + "\"}";
        MvcResult result =  mvc.perform(
                put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(authorRepository, times(1)).save(any());

        ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(argument.capture());
        assertEquals(TEST_NEW_AUTHOR_NAME, argument.getValue().getName());
    }

    @DisplayName("должен удалять автора методом DELETE по URL /api/authors/:id")
    @Test
    public void testDeleteApi() throws Exception {
        val author = new Author(1, TEST_AUTHOR_NAME);
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        mvc.perform(delete("/api/authors/1")).andExpect(status().isOk());
        verify(authorRepository, times(1)).deleteById(any());
    }

    @DisplayName("должен возвращать ошибку если удалять не существующиего автора методом DELETE по URL /api/authors/:id")
    @Test
    public void testDeleteErrorApi() throws Exception {
        mvc.perform(delete("/api/authors/1")).andExpect(status().is4xxClientError());
    }
}
