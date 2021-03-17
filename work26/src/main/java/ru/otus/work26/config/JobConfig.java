package ru.otus.work26.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.work26.domain.*;
import ru.otus.work26.service.TableOpsTasklet;
import ru.otus.work26.service.TransformService;

import javax.sql.DataSource;

@Configuration
public class JobConfig {

    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String OUTPUT_FOLDER = "./output";
    public static final String LAUNCH_TIME = "launchTime";
    public static final String OUTPUT_AUTHOR_FILE_NAME = "outputAuthorFileName";
    public static final String OUTPUT_GENRE_FILE_NAME = "outputGenreFileName";
    public static final String OUTPUT_BOOK_FILE_NAME = "outputBookFileName";
    public static final String OUTPUT_COMMENTS_FILE_NAME = "outputCommentsFileName";

    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job importLibraryJob(Step stepMigrateAuthor, Step stepMigrateGenre, Step stepMigrateBook, Step stepMigrateComment, Step cleanFolderTasklet) {
        return jobBuilderFactory.get(IMPORT_LIBRARY_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(databaseOpsTasklet("DELETE FROM comments"))
                .next(databaseOpsTasklet("DELETE FROM books"))
                .next(databaseOpsTasklet("DELETE FROM authors"))
                .next(databaseOpsTasklet("DELETE FROM genres"))
                .next(cleanFolderTasklet)
                .next(stepMigrateAuthor)
                .next(stepMigrateGenre)
                .next(stepMigrateBook)
                .next(stepMigrateComment)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, AuthorMigrate> processorAuthor(TransformService transformService) {
        return transformService::transformAuthor;
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, GenreMigrate> processorGenre(TransformService transformService) {
        return transformService::transformGenre;
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, BookMigrate> processorBook(TransformService transformService) {
        return transformService::transformBook;
    }

    @StepScope
    @Bean
    public ItemProcessor<Comment, CommentMigrate> processorComment(TransformService transformService) {
        return transformService::transformComment;
    }

    public Step databaseOpsTasklet(String sql) {
        return stepBuilderFactory.get("loadIntoProcessingTable")
                .tasklet(tasklet(sql))
                .allowStartIfComplete(true)
                .build();
    }

    public Tasklet tasklet(String sql) {
        return new TableOpsTasklet(sql, dataSource);
    }


}
