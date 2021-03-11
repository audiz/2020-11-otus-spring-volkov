package ru.otus.work25.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.work25.domain.Author;
import ru.otus.work25.domain.Book;
import ru.otus.work25.domain.Genre;
import ru.otus.work25.repositories.AuthorRepository;
import ru.otus.work25.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// From @DataMongoTest
@ExtendWith({SpringExtension.class})
@OverrideAutoConfiguration(
        enabled = false
)
@TypeExcludeFilters({DataMongoTypeExcludeFilter.class})
@AutoConfigureCache
@AutoConfigureDataMongo
@ImportAutoConfiguration
// From @DataMongoTest

@EnableConfigurationProperties
@ComponentScan({"ru.otus.work25.service", "ru.otus.work25.domain", "ru.otus.work25.changelog", "ru.otus.work25.security"})
@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;

    @Test
    public void testBooksAnyAuthorization() throws Exception {
        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_USER"})
    @Test
    public void testInsertBookWithAuthorization() throws Exception {
        given(authorRepository.findById(1L)).willReturn(Optional.of(new Author(1, "Author")));
        given(genreRepository.findById(1L)).willReturn(Optional.of(new Genre(1, "Genre")));

        val book = new Book(1, "TEST_BOOK_NAME", new Genre(1, "Genre"), new Author(1, "Author"), new ArrayList<>());
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(book))).andExpect(status().isOk());
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Test
    public void testInsertBookWithoutAuthorization() throws Exception {
        given(authorRepository.findById(1L)).willReturn(Optional.of(new Author(1, "Author")));
        given(genreRepository.findById(1L)).willReturn(Optional.of(new Genre(1, "Genre")));

        val book = new Book(1, "TEST_BOOK_NAME", new Genre(1, "Genre"), new Author(1, "Author"), new ArrayList<>());
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(book))).andExpect(status().is4xxClientError());
    }
}
