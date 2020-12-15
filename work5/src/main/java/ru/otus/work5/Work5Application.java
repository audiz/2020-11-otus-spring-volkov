package ru.otus.work5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.work5.configs.AppProps;
import ru.otus.work5.service.QuestionService;

@EnableConfigurationProperties(AppProps.class)
@SpringBootApplication
public class Work5Application {

	public static void main(String[] args) {
		SpringApplication.run(Work5Application.class, args);
	}

}
