package ru.otus.work17.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import ru.otus.work17.domain.Author;
import ru.otus.work17.domain.Book;
import ru.otus.work17.domain.Comment;
import ru.otus.work17.domain.Genre;
import ru.otus.work17.exceptions.AlreadyExistException;
import ru.otus.work17.exceptions.NotFoundException;
import ru.otus.work17.repositories.AuthorRepository;
import ru.otus.work17.repositories.BookRepository;
import ru.otus.work17.repositories.CommentRepository;
import ru.otus.work17.repositories.GenreRepository;
import ru.otus.work17.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final SequenceGeneratorService service;


    @GetMapping("/api/books")
    public CollectionModel<EntityModel<Book>> all() {
        var books = bookRepository.findAll();
        List<EntityModel<Book>> entityModels = new ArrayList<>();
        for (final Book book : books) {
            EntityModel<Book> model = EntityModel.of(book);
            model.add(linkTo(methodOn(CommentController.class).all(book.getId())).withRel("commentList"));
            entityModels.add(model);
        }
        CollectionModel<EntityModel<Book>> model = CollectionModel.of(entityModels);
        return model;
    }

    @GetMapping("/api/books/{id}")
    public EntityModel<Book> get(@PathVariable long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        var model = EntityModel.of(book);
        var link = linkTo(methodOn(CommentController.class).all(id)).withRel("commentList");
        model.add(link);
        return model;
    }

    @PostMapping("/api/books")
    public EntityModel<Book> create(@RequestBody Book book) {
        if(bookRepository.isExistsByTitleRegexIgnoreCase(book.getTitle())) {
            throw new AlreadyExistException("Book with title '"+ book.getTitle() +"' already exists");
        }

        Optional.ofNullable(book.getAuthor()).orElseThrow(() -> new NotFoundException("Please select author"));
        Optional.ofNullable(book.getGenre()).orElseThrow(() -> new NotFoundException("Please select genre"));
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(() -> new NotFoundException("Author not found"));
        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Genre not found"));
        book.setAuthor(author);
        book.setGenre(genre);

        book.setComments(new ArrayList<>());
        book.setId(service.getSequenceNumber(Book.SEQUENCE_NAME));
        bookRepository.save(book);
        return EntityModel.of(book);
    }



    @PutMapping("/api/books/{id}")
    public EntityModel<Book> save(@RequestBody Book book, @PathVariable long id) {
        var oldBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));

        Optional.ofNullable(book.getAuthor()).orElseThrow(() -> new NotFoundException("Please select author"));
        Optional.ofNullable(book.getGenre()).orElseThrow(() -> new NotFoundException("Please select genre"));
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(() -> new NotFoundException("Author not found"));
        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Genre not found"));
        book.setAuthor(author);
        book.setGenre(genre);

        book.setComments(oldBook.getComments());
        book.setId(id);
        bookRepository.save(book);
        return EntityModel.of(book);
    }

    @DeleteMapping("/api/books/{id}")
    public void delete(@PathVariable("id") long id) {
        bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        bookRepository.deleteById(id);
    }
}
