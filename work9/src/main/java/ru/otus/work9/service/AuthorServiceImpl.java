package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Author;
import ru.otus.work9.repositories.AuthorRepository;
import ru.otus.work9.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthorServiceImpl implements AuthorService {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    @Transactional
    @Override
    public String insertAuthor(String name) {
        authorRepo.insert(new Author(0, name));
        return "Success";
    }

    @Transactional
    @Override
    public String updateAuthor(Long id, String name) {
        Author author = authorRepo.update(new Author(id, name));
        return "Author updated = " + author;
    }

    @Transactional
    @Override
    public String deleteAuthor(Long id) {
        Optional<Author> author = authorRepo.findById(id);
        if(author.isEmpty()) {
            return "Author not found";
        }
        System.out.println("bookRepo.getCountAuthorById = " + bookRepo.getCountByAuthorId(id));
        if(bookRepo.getCountByAuthorId(id) > 0) {
            return "You can't delete an author because books with him exist";
        }
        authorRepo.delete(author.get());
        return "Author deleted";

    }

    @Override
    public String listAuthors() {
        List<Author> genres = authorRepo.getAll();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME");
        at.addRule();
        genres.forEach(genre -> {
            at.addRow(genre.getId(), genre.getName());
            at.addRule();
        });
        return at.render();
    }

    @Override
    public String getAuthor(Long id) {
        Optional<Author> author = authorRepo.findById(id);
        if(author.isEmpty()) {
            return "Author not found";
        } else {
            return author.get().toString();
        }
    }
}
