package ru.otus.work16.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.work16.domain.Book;
import ru.otus.work16.repositories.AuthorRepository;
import ru.otus.work16.repositories.BookRepository;
import ru.otus.work16.repositories.GenreRepository;
import ru.otus.work16.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final SequenceGeneratorService service;

    @GetMapping("/books")
    public String listBooks(Model model) {
        List<Book> persons = bookRepository.findAll();
        model.addAttribute("books", persons);
        return "listBooks";
    }

    @GetMapping("/books/{id}")
    public String showBook(@PathVariable("id") long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "showBook";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook(@PathVariable("id") long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        var authors = authorRepository.findAll();
        var genres = genreRepository.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editBook";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        var authors = authorRepository.findAll();
        var genres = genreRepository.findAll();
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editBook";
    }


    @RequestMapping(value = "/books/save", method= RequestMethod.POST)
    public String saveBook(@ModelAttribute(value="book") Book book) {
        var bookOrig = bookRepository.findById(book.getId());
        bookOrig.ifPresent(value -> book.setComments(value.getComments()));
        if(book.getId() == 0) {
            book.setId(service.getSequenceNumber(Book.SEQUENCE_NAME));
        }
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        bookRepository.delete(book);
        return "redirect:/books";
    }


}
