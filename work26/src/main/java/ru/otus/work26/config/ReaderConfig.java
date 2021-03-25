package ru.otus.work26.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.work26.domain.Author;
import ru.otus.work26.domain.Book;
import ru.otus.work26.domain.Comment;
import ru.otus.work26.domain.Genre;

import java.util.HashMap;

@Configuration
public class ReaderConfig {

    @StepScope
    @Bean
    public MongoItemReader<Author> authorReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<Author>()
                .name("authorItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(Author.class)
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public MongoItemReader<Genre> genreReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<Genre>()
                .name("genreItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(Genre.class)
                .sorts(new HashMap<>())
                .build();
    }


    @StepScope
    @Bean
    public MongoItemReader<Book> bookReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<Book>()
                .name("bookItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(Book.class)
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public MongoItemReader<Comment> commentReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<Comment>()
                .name("commentItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(Comment.class)
                .sorts(new HashMap<>())
                .build();
    }

}
