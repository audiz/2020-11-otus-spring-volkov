package ru.otus.work26.service;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.File;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

    private String path;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        FileUtils.forceMkdir(new File(path));
        FileUtils.cleanDirectory(new File(path));
        return RepeatStatus.FINISHED;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(path, "directory must be set");
    }
}