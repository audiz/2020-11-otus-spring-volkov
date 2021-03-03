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
import ru.otus.work20.domain.Genre;
import ru.otus.work20.repositories.AuthorRepository;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorControllerTest {

    @Autowired
    private RouterFunction routeAuthor;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private SequenceGeneratorService service;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String TEST_AUTHOR_NAME = "Test Genre String";

    @SneakyThrows
    @Test
    public void testGetRoute() {

        val author = new Author(1L, TEST_AUTHOR_NAME);

        given(authorRepository.findAll()).willReturn(Flux.just(author));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeAuthor)
                .build();

        client.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(new Author[] { author }));
    }

    @SneakyThrows
    @Test
    public void testPostRoute() {

        val author = new Author(3L, TEST_AUTHOR_NAME);

        given(authorRepository.isExistsByNameRegexIgnoreCase(any())).willReturn(Mono.just(false));
        given(service.getReactiveSequenceNumber(any())).willReturn(Mono.just(3L));
        given(authorRepository.save(any())).willReturn(Mono.just(author));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeAuthor)
                .build();

        client.post()
                .uri("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(author))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(author));
    }

    @SneakyThrows
    @Test
    public void testPutRoute() {

        val author = new Author(3L, TEST_AUTHOR_NAME);

        given(authorRepository.findById(any(Long.class))).willReturn(Mono.just(author));
        given(authorRepository.save(any())).willReturn(Mono.just(author));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeAuthor)
                .build();

        client.put()
                .uri("/api/authors/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(author))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(author));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoute() {

        val author = new Author(3L, TEST_AUTHOR_NAME);

        given(bookRepository.isExistsByAuthorId(any(Long.class))).willReturn(Mono.just(false));
        given(authorRepository.findById(any(Long.class))).willReturn(Mono.just(author));
        given(authorRepository.delete(any())).willReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeAuthor)
                .build();

        client.delete()
                .uri("/api/authors/3")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .isEmpty();
    }
}
