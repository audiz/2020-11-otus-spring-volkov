package ru.otus.work23.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ru.otus.work23.domain.Genre;
import ru.otus.work23.exceptions.AlreadyExistException;
import ru.otus.work23.exceptions.NotFoundException;
import ru.otus.work23.repositories.BookRepository;
import ru.otus.work23.repositories.GenreRepository;
import ru.otus.work23.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    @GetMapping("/api/genres")
    public CollectionModel<Genre> all() {
        List<Genre> genres = genreRepository.findAll();
        return CollectionModel.of(genres);
    }

    @PostMapping("/api/genres")
    public EntityModel<Genre> create(@RequestBody Genre genre) {
        if(genreRepository.isExistsByNameRegexIgnoreCase(genre.getName())) {
            throw new AlreadyExistException("Genre with name '"+ genre.getName() +"' already exists");
        }
        genre.setId(service.getSequenceNumber(Genre.SEQUENCE_NAME));
        genreRepository.save(genre);
        return EntityModel.of(genre);
    }

    @PutMapping("/api/genres/{id}")
    public EntityModel<Genre> save(@RequestBody Genre genre, @PathVariable long id) {
        genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre not found"));
        genre.setId(id);
        genreRepository.save(genre);
        return EntityModel.of(genre);
    }

    @DeleteMapping("/api/genres/{id}")
    public void delete(@PathVariable("id") long id) {
        genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre not found"));
        if(bookRepository.isExistsByGenreId(id)){
            throw new AlreadyExistException("Genre linked for books");
        }
        genreRepository.deleteById(id);
    }
}
