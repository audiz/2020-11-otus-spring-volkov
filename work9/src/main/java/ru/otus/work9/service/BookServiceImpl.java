package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Author;
import ru.otus.work9.domain.Book;
import ru.otus.work9.domain.Genre;
import ru.otus.work9.repositories.AuthorRepository;
import ru.otus.work9.repositories.BookRepository;
import ru.otus.work9.repositories.CommentRepository;
import ru.otus.work9.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final GenreRepository genreRepo;
    private final CommentRepository commentRepo;

    @Override
    public String deleteBook(Long id) {
        if(bookRepo.findById(id).isEmpty()) {
            return "Book not found";
        }
        int result = bookRepo.deleteById(id);
        if(result == 1) {
            return "Book deleted";
        }
        return "Can't delete";
    }

    @Override
    public String listBooks() {
        List<Book> books = bookRepo.getAll();
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
        Optional<Book> book = bookRepo.getFullById(id);
        if(book.isEmpty()) {
            return "Book not found";
        } else {
            return book.get().toString();
        }
    }

    @Override
    public String insertBook(Long id, String title, Long genre, Long author) {
        if(bookRepo.findById(id).isPresent()) {
            return "Book already exists";
        }
        Optional<Genre> optionalGenre = genreRepo.findById(genre);
        if(optionalGenre.isEmpty()) {
            return "Genre not found";
        }
        Optional<Author> optionalAuthor = authorRepo.findById(author);
        if(optionalAuthor.isEmpty()) {
            return "Author not found";
        }
        bookRepo.insert(new Book(id, title, optionalGenre.get(), optionalAuthor.get(), null));
        return "Success";
    }

    @Override
    public String updateBook(Long id, String title, Long genre, Long author) {
        if(bookRepo.findById(id).isEmpty()) {
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
        bookRepo.update(new Book(id, title, genreDaoById.get(), authorDaoById.get(), null));
        return "Success";
    }
}
