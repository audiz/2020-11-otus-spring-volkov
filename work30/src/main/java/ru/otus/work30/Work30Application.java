package ru.otus.work30;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMongock
@EnableMongoRepositories
@SpringBootApplication
/*@EnableWebMvc*/
public class Work30Application {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Work30Application.class);
	}
}
