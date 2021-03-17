package ru.otus.work26.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.otus.work26.domain.AuthorMigrate;
import ru.otus.work26.domain.BookMigrate;
import ru.otus.work26.domain.CommentMigrate;
import ru.otus.work26.domain.GenreMigrate;

import javax.sql.DataSource;
import java.util.Arrays;

import static ru.otus.work26.config.JobConfig.LAUNCH_TIME;
import static ru.otus.work26.config.JobConfig.OUTPUT_FOLDER;
import static ru.otus.work26.config.JobConfig.OUTPUT_AUTHOR_FILE_NAME;
import static ru.otus.work26.config.JobConfig.OUTPUT_GENRE_FILE_NAME;
import static ru.otus.work26.config.JobConfig.OUTPUT_BOOK_FILE_NAME;
import static ru.otus.work26.config.JobConfig.OUTPUT_COMMENTS_FILE_NAME;


@Configuration
public class WriterConfig {

    @Bean
    public JdbcBatchItemWriter<AuthorMigrate> authorWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<AuthorMigrate> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO authors (id, name) VALUES (:id, :name)");
        writer.setItemSqlParameterSourceProvider(item -> {
            final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", item.getId());
            sqlParameterSource.addValue("name", item.getName());
            return sqlParameterSource;
        });
        writer.afterPropertiesSet();
        return writer;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<AuthorMigrate> authorCsvWriter(@Value("#{jobParameters['" + OUTPUT_AUTHOR_FILE_NAME + "']}") String outputFileName)
    {
        FlatFileItemWriter<AuthorMigrate> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(OUTPUT_FOLDER + "/" + outputFileName));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[] { "id", "name" });
                    }
                });
            }
        });
        return writer;
    }

    @StepScope
    @Bean
    public CompositeItemWriter<AuthorMigrate> compositeAuthorWriter(final DataSource dataSource, @Value("#{jobParameters['" + OUTPUT_AUTHOR_FILE_NAME + "']}") String outputFileName,
                                                                    @Value("#{jobParameters['" + LAUNCH_TIME + "']}") String launchTime) {
        CompositeItemWriter<AuthorMigrate> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(authorWriter(dataSource), authorCsvWriter(outputFileName)));
        return compositeItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<GenreMigrate> genreWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<GenreMigrate> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO genres (id, name) VALUES (:id, :name)");
        writer.setItemSqlParameterSourceProvider(item -> {
            final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", item.getId());
            sqlParameterSource.addValue("name", item.getName());
            return sqlParameterSource;
        });
        writer.afterPropertiesSet();
        return writer;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<GenreMigrate> genreCsvWriter(@Value("#{jobParameters['" + OUTPUT_GENRE_FILE_NAME + "']}") String outputFileName)
    {
        FlatFileItemWriter<GenreMigrate> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(OUTPUT_FOLDER + "/" + outputFileName));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[] { "id", "name"});
                    }
                });
            }
        });
        return writer;
    }

    @StepScope
    @Bean
    public CompositeItemWriter<GenreMigrate> compositeGenreWriter(final DataSource dataSource, @Value("#{jobParameters['" + OUTPUT_GENRE_FILE_NAME + "']}") String outputFileName,
                                                                  @Value("#{jobParameters['" + LAUNCH_TIME + "']}") String launchTime) {
        CompositeItemWriter<GenreMigrate> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(genreWriter(dataSource), genreCsvWriter(outputFileName)));
        return compositeItemWriter;
    }


    @Bean
    public JdbcBatchItemWriter<BookMigrate> bookWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<BookMigrate> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO books (id, title, genre, author) VALUES (:id, :title, :genre, :author)");
        writer.setItemSqlParameterSourceProvider(item -> {
            final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", item.getId());
            sqlParameterSource.addValue("title", item.getTitle());
            sqlParameterSource.addValue("genre", item.getGenreId());
            sqlParameterSource.addValue("author", item.getAuthorId());
            return sqlParameterSource;
        });
        writer.afterPropertiesSet();
        return writer;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<BookMigrate> bookCsvWriter(@Value("#{jobParameters['" + OUTPUT_BOOK_FILE_NAME + "']}") String outputFileName)
    {
        FlatFileItemWriter<BookMigrate> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(OUTPUT_FOLDER + "/" + outputFileName));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[] { "id", "title", "genreId", "authorId"});
                    }
                });
            }
        });
        return writer;
    }

    @StepScope
    @Bean
    public CompositeItemWriter<BookMigrate> compositeBookWriter(final DataSource dataSource, @Value("#{jobParameters['" + OUTPUT_BOOK_FILE_NAME + "']}") String outputFileName,
                                                                @Value("#{jobParameters['" + LAUNCH_TIME + "']}") String launchTime) {
        CompositeItemWriter<BookMigrate> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(bookWriter(dataSource), bookCsvWriter(outputFileName)));
        return compositeItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<CommentMigrate> commentWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<CommentMigrate> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO comments (id, comment, book_id) VALUES (:id, :comment, :book_id)");
        writer.setItemSqlParameterSourceProvider(item -> {
            final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", item.getId());
            sqlParameterSource.addValue("comment", item.getComment());
            sqlParameterSource.addValue("book_id", item.getBookId());
            return sqlParameterSource;
        });
        writer.afterPropertiesSet();
        return writer;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<CommentMigrate> commentCsvWriter(@Value("#{jobParameters['" + OUTPUT_COMMENTS_FILE_NAME + "']}") String outputFileName)
    {
        FlatFileItemWriter<CommentMigrate> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(OUTPUT_FOLDER + "/" + outputFileName));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[] { "id", "comment", "bookId"});
                    }
                });
            }
        });
        return writer;
    }

    @StepScope
    @Bean
    public CompositeItemWriter<CommentMigrate> compositeCommentWriter(final DataSource dataSource, @Value("#{jobParameters['" + OUTPUT_COMMENTS_FILE_NAME + "']}") String outputFileName,
                                                                      @Value("#{jobParameters['" + LAUNCH_TIME + "']}") String launchTime) {
        CompositeItemWriter<CommentMigrate> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(commentWriter(dataSource), commentCsvWriter(outputFileName)));
        return compositeItemWriter;
    }

}
