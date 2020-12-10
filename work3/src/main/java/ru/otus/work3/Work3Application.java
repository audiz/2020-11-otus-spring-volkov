package ru.otus.work3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.work3.configs.AppProps;
import ru.otus.work3.service.QuestionService;

@EnableConfigurationProperties(AppProps.class)
@SpringBootApplication
public class Work3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Work3Application.class, args);
		QuestionService service = context.getBean(QuestionService.class);
		service.doQuestions();
	}

}
