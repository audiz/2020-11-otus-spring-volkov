package ru.otus.work20.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SummaryControllerTest {

    @Autowired
    private RouterFunction routeSummary;

    @Test
    public void testRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(routeSummary)
                .build();

        client.get()
                .uri("/api/summary")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"books\":0,\"authors\":0,\"genres\":0,\"comments\":0}");
    }


}
