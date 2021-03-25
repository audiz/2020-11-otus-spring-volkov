package ru.otus.work26.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppProps {
    private String inputFile;
    private String outputAuthorFile;
    private String outputGenreFile;
    private String outputBookFile;
    private String outputCommentFile;
}
