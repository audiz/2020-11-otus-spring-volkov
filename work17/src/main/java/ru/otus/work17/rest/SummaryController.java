package ru.otus.work17.rest;

import lombok.val;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ru.otus.work17.domain.Summary;
import ru.otus.work17.repositories.AuthorRepository;
import ru.otus.work17.repositories.BookRepository;
import ru.otus.work17.repositories.CommentRepository;
import ru.otus.work17.repositories.GenreRepository;

@Log4j2
@RequiredArgsConstructor
@RestController
public class SummaryController {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/api/summary")
    public EntityModel<Summary> all() {
        val summary = new Summary();
        summary.setAuthors(authorRepository.count());
        summary.setBooks(bookRepository.count());
        summary.setGenres(genreRepository.count());
        summary.setComments(commentRepository.count());
        return EntityModel.of(summary);
    }
}
