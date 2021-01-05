package ru.otus.work9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.work9.configs.AppProps;

@EnableConfigurationProperties(AppProps.class)
@SpringBootApplication
public class Work9Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Work9Application.class);
		//Console.main(args);
	}

}
