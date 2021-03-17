package ru.otus.work25.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.otus.work25.domain.Book;
import ru.otus.work25.dto.BookDto;
import ru.otus.work25.exceptions.AlreadyExistException;
import ru.otus.work25.exceptions.NotFoundException;
import ru.otus.work25.repositories.AuthorRepository;
import ru.otus.work25.repositories.BookRepository;
import ru.otus.work25.repositories.GenreRepository;
import ru.otus.work25.security.PermissionService;
import ru.otus.work25.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PermissionService permissionService;

    @GetMapping("/api/books")
    public CollectionModel<EntityModel<BookDto>> all() {
        var books = bookRepository.findAll();
        List<EntityModel<BookDto>> entityModels = new ArrayList<>();
        for (final Book book : books) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<String> authorities = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());

            boolean hasReadPermission = permissionService.hasReadPermissions(book.getId(), Book.class.getCanonicalName());
            boolean hasWritePermission = permissionService.hasWritePermissions(book.getId(), Book.class.getCanonicalName());
            log.debug("name = {}, authorities = {}, hasReadPermission = {}, hasWritePermission = {}", userDetails.getUsername(), authorities, hasReadPermission, hasWritePermission);

            EntityModel<BookDto> model = EntityModel.of(new BookDto(book, hasReadPermission, hasWritePermission));

            model.add(linkTo(methodOn(CommentController.class).all(book.getId())).withRel("commentList"));
            entityModels.add(model);
        }
        CollectionModel<EntityModel<BookDto>> model = CollectionModel.of(entityModels);
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
