package ru.otus.work20.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.Author;
import ru.otus.work20.domain.Book;
import ru.otus.work20.domain.Genre;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookControllerTest {

    @Autowired
    private RouterFunction routeBook;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private SequenceGeneratorService service;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String TEST_BOOK_NAME = "Test Book String";

    @SneakyThrows
    @Test
    public void testGetRoute() {
        val genre = new Genre(1L, "genre");
        val author = new Author(1L, "author");
        val book = new Book(1L, TEST_BOOK_NAME, genre, author, new ArrayList<>());

        given(bookRepository.findAll()).willReturn(Flux.just(book));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeBook)
                .build();

        client.get()
                .uri("/api/books")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(new Book[] { book }));
    }

    @SneakyThrows
    @Test
    public void testPostRoute() {

        val genre = new Genre(3L, "genre");
        val author = new Author(3L, "author");
        val book = new Book(3L, TEST_BOOK_NAME, genre, author, new ArrayList<>());

        given(genreRepository.findById(any(Long.class))).willReturn(Mono.just(genre));
        given(authorRepository.findById(any(Long.class))).willReturn(Mono.just(author));
        given(bookRepository.isExistsByTitleRegexIgnoreCase(any())).willReturn(Mono.just(false));
        given(service.getReactiveSequenceNumber(any())).willReturn(Mono.just(3L));
        given(bookRepository.save(any())).willReturn(Mono.just(book));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeBook)
                .build();

        client.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(book));
    }

    @SneakyThrows
    @Test
    public void testPutRoute() {

        val genre = new Genre(3L, "genre");
        val author = new Author(3L, "author");
        val book = new Book(3L, TEST_BOOK_NAME, genre, author, new ArrayList<>());

        given(genreRepository.findById(any(Long.class))).willReturn(Mono.just(genre));
        given(authorRepository.findById(any(Long.class))).willReturn(Mono.just(author));
        given(bookRepository.findById(any(Long.class))).willReturn(Mono.just(book));
        given(bookRepository.save(any())).willReturn(Mono.just(book));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeBook)
                .build();

        client.put()
                .uri("/api/books/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(book));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoute() {

        val genre = new Genre(3L, "genre");
        val author = new Author(3L, "author");
        val book = new Book(3L, TEST_BOOK_NAME, genre, author, new ArrayList<>());

        given(bookRepository.findById(any(Long.class))).willReturn(Mono.just(book));
        given(bookRepository.delete(any())).willReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeBook)
                .build();

        client.delete()
                .uri("/api/books/3")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .isEmpty();
    }
}
