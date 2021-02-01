package ru.otus.work13.repositories;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.work13.service", "ru.otus.work13.domain", "ru.otus.work13.changelog"})
public abstract class AbstractRepositoryTest {
}
