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

@DisplayName("GenreController тест REST API жанров")
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GenreController.class, excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class GenreControllerTests {
    public static final String TEST_GENRE_NAME = "Test Genre String";
    public static final String TEST_NEW_GENRE_NAME = "NEW Genre String";
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

    @DisplayName("должен находить жанр методом GET по URL /api/genres")
    @Test
    public void testGetApi() throws Exception {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, TEST_GENRE_NAME));

        given(genreRepository.findAll()).willReturn(genres);
        MvcResult result =  mvc.perform(get("/api/genres")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(TEST_GENRE_NAME);
    }

    @DisplayName("должен добавлять жанр методом POST по URL /api/genres")
    @Test
    public void testPostApi() throws Exception {
        String insertJson = "{\"id\":0,\"name\":\"" + TEST_GENRE_NAME + "\"}";
        MvcResult result =  mvc.perform(
                post("/api/genres")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(genreRepository, times(1)).save(any());

        ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
        verify(genreRepository).save(argument.capture());
        assertEquals(TEST_GENRE_NAME, argument.getValue().getName());
    }

    @DisplayName("должен изменять жанр методом PUT по URL /api/genres/:id")
    @Test
    public void testPutApi() throws Exception {
        val genre = new Genre(1, TEST_GENRE_NAME);
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));

        String insertJson = "{\"id\":1,\"name\":\"" + TEST_NEW_GENRE_NAME + "\"}";
        MvcResult result =  mvc.perform(
                put("/api/genres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertJson)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(insertJson);

        verify(genreRepository, times(1)).save(any());

        ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
        verify(genreRepository).save(argument.capture());
        assertEquals(TEST_NEW_GENRE_NAME, argument.getValue().getName());
    }

    @DisplayName("должен удалять жанр методом DELETE по URL /api/genres/:id")
    @Test
    public void testDeleteApi() throws Exception {
        val genre = new Genre(1, TEST_GENRE_NAME);
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        mvc.perform(delete("/api/genres/1")).andExpect(status().isOk());
        verify(genreRepository, times(1)).deleteById(any());
    }

    @DisplayName("должен возвращать ошибку если удалять не существующий жанр методом DELETE по URL /api/genres/:id")
    @Test
    public void testDeleteErrorApi() throws Exception {
        mvc.perform(delete("/api/genres/1")).andExpect(status().is4xxClientError());
    }
}
