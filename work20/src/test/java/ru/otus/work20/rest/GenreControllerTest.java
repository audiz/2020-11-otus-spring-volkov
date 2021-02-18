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
import ru.otus.work20.domain.Genre;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.GenreRepository;
import ru.otus.work20.service.SequenceGeneratorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GenreControllerTest {

    @Autowired
    private RouterFunction routeGenre;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private SequenceGeneratorService service;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String TEST_GENRE_NAME = "Test Genre String";

    @SneakyThrows
    @Test
    public void testGetRoute() {

        val genre = new Genre(1L, TEST_GENRE_NAME);

        given(genreRepository.findAll()).willReturn(Flux.just(genre));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeGenre)
                .build();

        client.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(new Genre[] { genre }));
    }

    @SneakyThrows
    @Test
    public void testPostRoute() {

        val genre = new Genre(3L, TEST_GENRE_NAME);

        given(genreRepository.isExistsByNameRegexIgnoreCase(any())).willReturn(Mono.just(false));
        given(service.getReactiveSequenceNumber(any())).willReturn(Mono.just(3L));
        given(genreRepository.save(any())).willReturn(Mono.just(genre));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeGenre)
                .build();

        client.post()
                .uri("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(genre))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(genre));
    }

    @SneakyThrows
    @Test
    public void testPutRoute() {

        val genre = new Genre(3L, TEST_GENRE_NAME);

        given(genreRepository.findById(any(Long.class))).willReturn(Mono.just(genre));
        given(genreRepository.save(any())).willReturn(Mono.just(genre));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeGenre)
                .build();

        client.put()
                .uri("/api/genres/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(genre))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(objectMapper.writeValueAsString(genre));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoute() {

        val genre = new Genre(3L, TEST_GENRE_NAME);

        given(bookRepository.isExistsByGenreId(any(Long.class))).willReturn(Mono.just(false));
        given(genreRepository.findById(any(Long.class))).willReturn(Mono.just(genre));
        given(genreRepository.delete(any())).willReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeGenre)
                .build();

        client.delete()
                .uri("/api/genres/3")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .isEmpty();
    }
}
