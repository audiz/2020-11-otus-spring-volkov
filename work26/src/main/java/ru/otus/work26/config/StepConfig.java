package ru.otus.work26.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.work26.domain.*;
import ru.otus.work26.service.FileDeletingTasklet;

import static ru.otus.work26.config.JobConfig.OUTPUT_FOLDER;

@Configuration
public class StepConfig {
    private static final int CHUNK_SIZE = 5;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step cleanFolderTasklet() {
        FileDeletingTasklet task = new FileDeletingTasklet();
        task.setPath(OUTPUT_FOLDER);
        return stepBuilderFactory.get("cleanFolderTasklet")
                .tasklet(task)
                .build();
    }


    @Bean
    public Step stepMigrateAuthor(CompositeItemWriter<AuthorMigrate> writer, ItemReader<Author> reader, ItemProcessor<Author, AuthorMigrate> itemProcessor) {
        return stepBuilderFactory.get("stepMigrateAuthor")
                .<Author, AuthorMigrate> chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepMigrateGenre(CompositeItemWriter<GenreMigrate> writer, ItemReader<Genre> reader, ItemProcessor<Genre, GenreMigrate> itemProcessor) {
        return stepBuilderFactory.get("stepMigrateGenre")
                .<Genre, GenreMigrate> chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepMigrateBook(CompositeItemWriter<BookMigrate> writer, ItemReader<Book> reader, ItemProcessor<Book, BookMigrate> itemProcessor) {
        return stepBuilderFactory.get("stepMigrateBook")
                .<Book, BookMigrate> chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepMigrateComment(CompositeItemWriter<CommentMigrate> writer, ItemReader<Comment> reader,  ItemProcessor<Comment, CommentMigrate> itemProcessor) {
        return stepBuilderFactory.get("stepMigrateComment")
                .<Comment, CommentMigrate> chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

}
