package ru.otus.work23.rest;

import lombok.val;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.otus.work23.domain.AuthInfo;
import ru.otus.work23.domain.Summary;
import ru.otus.work23.repositories.AuthorRepository;
import ru.otus.work23.repositories.BookRepository;
import ru.otus.work23.repositories.CommentRepository;
import ru.otus.work23.repositories.GenreRepository;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ServiceController {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/api/summary")
    public EntityModel<Summary> summary() {
        val summary = new Summary();
        summary.setAuthors(authorRepository.count());
        summary.setBooks(bookRepository.count());
        summary.setGenres(genreRepository.count());
        summary.setComments(commentRepository.count());
        return EntityModel.of(summary);
    }

    @GetMapping("/api/authinfo")
    public EntityModel<AuthInfo> authinfo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();

        AuthInfo authInfo = new AuthInfo();
        authInfo.setAuthenticated(securityContext.getAuthentication().isAuthenticated());
        authInfo.setUsername(userDetails.getUsername());

        return EntityModel.of(authInfo);
    }
}
