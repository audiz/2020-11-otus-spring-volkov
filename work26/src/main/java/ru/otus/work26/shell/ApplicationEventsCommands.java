package ru.otus.work26.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.work26.config.AppProps;

import static ru.otus.work26.config.JobConfig.*;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final AppProps appProps;
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    @ShellMethod(value = "runJob", key = "rj")
    public void startMigrationJobWithJobOperator() throws Exception {
        Long executionId = jobOperator.start(IMPORT_LIBRARY_JOB_NAME,
                OUTPUT_AUTHOR_FILE_NAME + "=" + appProps.getOutputAuthorFile() + "\n" +
                          OUTPUT_GENRE_FILE_NAME + "=" + appProps.getOutputGenreFile() + "\n" +
                          OUTPUT_BOOK_FILE_NAME + "=" + appProps.getOutputBookFile() + "\n" +
                          OUTPUT_COMMENTS_FILE_NAME + "=" + appProps.getOutputCommentFile() + "\n" +
                          LAUNCH_TIME + "=" + System.currentTimeMillis());
        System.out.println(jobOperator.getSummary(executionId));
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_LIBRARY_JOB_NAME));
    }

}
