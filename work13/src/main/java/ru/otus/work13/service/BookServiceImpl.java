package ru.otus.work13.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work13.domain.Author;
import ru.otus.work13.domain.Book;
import ru.otus.work13.domain.Comment;
import ru.otus.work13.domain.Genre;
import ru.otus.work13.repositories.AuthorRepository;
import ru.otus.work13.repositories.BookRepository;
import ru.otus.work13.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final GenreRepository genreRepo;
    private final SequenceGeneratorService service;

    @Transactional
    @Override
    public String deleteBook(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        if(book.isEmpty()) {
            return "Book not found";
        }
        bookRepo.delete(book.get());
        return "Book deleted";
    }

    @Override
    public String listBooks() {
        List<Book> books = bookRepo.findAll();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "TITLE", "GENRE", "AUTHOR");
        at.addRule();
        books.forEach(book -> {
            at.addRow(book.getId(), book.getTitle(), book.getGenre().getName(), book.getAuthor().getName());
            at.addRule();
        });
        return at.render();
    }

    @Transactional
    @Override
    public String getBook(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        if(book.isEmpty()) {
            return "Book not found";
        } else {
            return book.get().toString() + "\r\nComments: " + Arrays.asList(book.get().getComments().toArray()).toString();
        }
    }

    @Transactional
    @Override
    public String insertBook(String title, Long genre, Long author) {
        Optional<Genre> optionalGenre = genreRepo.findById(genre);
        if(optionalGenre.isEmpty()) {
            return "Genre not found";
        }
        Optional<Author> optionalAuthor = authorRepo.findById(author);
        if(optionalAuthor.isEmpty()) {
            return "Author not found";
        }
        bookRepo.save(new Book(service.getSequenceNumber(Book.SEQUENCE_NAME), title, optionalGenre.get(), optionalAuthor.get(), null));
        return "Success";
    }

    @Transactional
    @Override
    public String updateBook(Long id, String title, Long genre, Long author) {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if(optionalBook.isEmpty()) {
            return "Book not found";
        }
        Optional<Genre> genreDaoById = genreRepo.findById(genre);
        if(genreDaoById.isEmpty()) {
            return "Genre not found";
        }
        Optional<Author> authorDaoById = authorRepo.findById(author);
        if(authorDaoById.isEmpty()) {
            return "Author not found";
        }

        optionalBook.get().setTitle(title);
        optionalBook.get().setGenre(genreDaoById.get());
        optionalBook.get().setAuthor(authorDaoById.get());

        bookRepo.save(optionalBook.get());
        return "Success";
    }
}
