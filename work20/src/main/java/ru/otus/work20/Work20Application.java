package ru.otus.work20;

import com.github.cloudyrock.spring.v5.EnableMongock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.reactive.function.server.RouterFunctions;
import ru.otus.work20.handlers.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Slf4j
@EnableMongock
@EnableMongoRepositories
@SpringBootApplication
public class Work20Application {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Work20Application.class);
	}

	@Bean
	public RouterFunction<ServerResponse> routeGenre(GenreHandler genreHandler) {
		return RouterFunctions.route(GET("/api/genres/{id}").and(accept(APPLICATION_JSON)), genreHandler::get)
				.andRoute(GET("/api/genres").and(accept(APPLICATION_JSON)), genreHandler::all)
				.andRoute(POST("/api/genres").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), genreHandler::post)
				.andRoute(PUT("/api/genres/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), genreHandler::put)
				.andRoute(DELETE("/api/genres/{id}"), genreHandler::delete);
	}

	@Bean
	public RouterFunction<ServerResponse> routeAuthor(AuthorHandler authorHandler) {
		return RouterFunctions.route(GET("/api/authors/{id}").and(accept(APPLICATION_JSON)), authorHandler::get)
				.andRoute(GET("/api/authors").and(accept(APPLICATION_JSON)), authorHandler::all)
				.andRoute(POST("/api/authors").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), authorHandler::post)
				.andRoute(PUT("/api/authors/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), authorHandler::put)
				.andRoute(DELETE("/api/authors/{id}"), authorHandler::delete);
	}

	@Bean
	public RouterFunction<ServerResponse> routeBook(BookHandler bookHandler) {
		return RouterFunctions.route(GET("/api/books/{id}").and(accept(APPLICATION_JSON)), bookHandler::get)
				.andRoute(GET("/api/books").and(accept(APPLICATION_JSON)), bookHandler::all)
				.andRoute(POST("/api/books").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), bookHandler::post)
				.andRoute(PUT("/api/books/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), bookHandler::put)
				.andRoute(DELETE("/api/books/{id}"), bookHandler::delete);
	}

	@Bean
	public RouterFunction<ServerResponse> routeComment(CommentHandler commentHandler) {
		return RouterFunctions.route(GET("/api/books/{bookId}/comments/{id}").and(accept(APPLICATION_JSON)), commentHandler::get)
				.andRoute(GET("/api/books/{bookId}/comments").and(accept(APPLICATION_JSON)), commentHandler::all)
				.andRoute(POST("/api/books/{bookId}/comments").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), commentHandler::post)
				.andRoute(DELETE("/api/books/{bookId}/comments/{id}"), commentHandler::delete);
	}

	@Bean
	public RouterFunction<ServerResponse> routeSummary(SummaryHandler summaryHandler) {
		return RouterFunctions.route(GET("/api/summary").and(accept(APPLICATION_JSON)), summaryHandler::all);
	}
}
