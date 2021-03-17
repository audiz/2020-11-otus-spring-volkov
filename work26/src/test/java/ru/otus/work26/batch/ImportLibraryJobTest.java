package ru.otus.work26.batch;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import static ru.otus.work26.config.JobConfig.OUTPUT_FOLDER;

@SpringBootTest
@SpringBatchTest
public class ImportLibraryJobTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    private static final String TEST_OUTPUT_AUTHOR_FILE_NAME = "author.csv";
    private static final String TEST_OUTPUT_GENRE_FILE_NAME = "genre.csv";
    private static final String TEST_OUTPUT_BOOK_FILE_NAME = "book.csv";
    private static final String TEST_OUTPUT_COMMENTS_FILE_NAME = "comment.csv";

    private static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";
    private static final String LAUNCH_TIME = "launchTime";

    private static final String OUTPUT_AUTHOR_FILE_NAME = "outputAuthorFileName";
    private static final String OUTPUT_GENRE_FILE_NAME = "outputGenreFileName";
    private static final String OUTPUT_BOOK_FILE_NAME = "outputBookFileName";
    private static final String OUTPUT_COMMENTS_FILE_NAME = "outputCommentsFileName";

    @BeforeEach
    void clearMetaData() { jobRepositoryTestUtils.removeJobExecutions(); }

    @Test
    void testJob() throws Exception {
        FileSystemResource actualAuthorResult = new FileSystemResource(OUTPUT_FOLDER + "/" + TEST_OUTPUT_AUTHOR_FILE_NAME);
        FileSystemResource actualGenreResult = new FileSystemResource(OUTPUT_FOLDER + "/" + TEST_OUTPUT_GENRE_FILE_NAME);
        FileSystemResource actualBookResult = new FileSystemResource(OUTPUT_FOLDER + "/" + TEST_OUTPUT_BOOK_FILE_NAME);
        FileSystemResource actualCommentsResult = new FileSystemResource(OUTPUT_FOLDER + "/" + TEST_OUTPUT_COMMENTS_FILE_NAME);

        Job job = jobLauncherTestUtils.getJob();
        Assertions.assertThat(job).isNotNull() .extracting(Job::getName).isEqualTo(IMPORT_LIBRARY_JOB_NAME);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addString(OUTPUT_AUTHOR_FILE_NAME, TEST_OUTPUT_AUTHOR_FILE_NAME)
                .addString(OUTPUT_GENRE_FILE_NAME, TEST_OUTPUT_GENRE_FILE_NAME)
                .addString(OUTPUT_BOOK_FILE_NAME, TEST_OUTPUT_BOOK_FILE_NAME)
                .addString(OUTPUT_COMMENTS_FILE_NAME, TEST_OUTPUT_COMMENTS_FILE_NAME)
                .addString(LAUNCH_TIME, String.valueOf(System.currentTimeMillis()))
                .toJobParameters());

        Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        AssertFile.assertLineCount(2, actualAuthorResult);
        AssertFile.assertLineCount(2, actualGenreResult);
        AssertFile.assertLineCount(2, actualBookResult);
        AssertFile.assertLineCount(2, actualCommentsResult);

    }

}
