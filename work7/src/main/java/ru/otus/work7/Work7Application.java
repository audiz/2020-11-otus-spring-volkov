package ru.otus.work7;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.work7.configs.AppProps;

@EnableConfigurationProperties(AppProps.class)
@SpringBootApplication
public class Work7Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Work7Application.class);
		//Console.main(args);
	}

}
