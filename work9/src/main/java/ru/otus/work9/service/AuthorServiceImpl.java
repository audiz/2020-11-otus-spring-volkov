package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

    @Override
    public String insertAuthor(Long id, String name) {
        if(authorRepo.findById(id).isPresent()) {
            return "Already exists";
        }
        authorRepo.insert(new Author(id, name));
        return "Success";
    }

    @Override
    public String updateAuthor(Long id, String name) {
        Author author = authorRepo.update(new Author(id, name));
        return "Author updated = " + author;
    }

    @Override
    public String deleteAuthor(Long id) {
        if(bookRepo.getCountAuthorById(id) > 0) {
            return "You can't delete an author because books with him exist";
        }
        if(authorRepo.findById(id).isEmpty()) {
            return "Author not found";
        }
        int result = authorRepo.deleteById(id);
        if(result == 1) {
            return "Author deleted";
        }
        return "Can't delete";
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
