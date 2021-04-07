package ru.otus.work30.actuators;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.work30.repositories.BookRepository;

import java.util.Random;

@AllArgsConstructor
@Component
public class BooksHealthHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long count = bookRepository.count();
        if (count == 0) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "No books in library")
                    .build();
        } else {
            return Health.up().withDetail("message", "Books exists").build();
        }
    }
}
