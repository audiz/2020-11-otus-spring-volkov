package ru.otus.work26.service;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class TableOpsTasklet implements Tasklet {

    private DataSource dataSource;

    private String sql;

    public TableOpsTasklet (String sql, DataSource dataSource) {
        this.sql = sql;
        this.dataSource = dataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        new JdbcTemplate(dataSource).execute(sql);
        return RepeatStatus.FINISHED;
    }

}