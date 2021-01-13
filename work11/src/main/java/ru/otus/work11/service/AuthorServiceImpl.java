package ru.otus.work11.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work11.domain.Author;
import ru.otus.work11.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepo;

    @Transactional
    @Override
    public String insertAuthor(String name) {
        authorRepo.save(new Author(0, name));
        return "Success";
    }

    @Transactional
    @Override
    public String updateAuthor(Long id, String name) {
        Optional<Author> author = authorRepo.findById(id);
        if(author.isEmpty()) {
            return "Author not found";
        }
        author.get().setName(name);
        authorRepo.save(author.get());
        return "Author updated = " + author;
    }

    @Transactional
    @Override
    public String deleteAuthor(Long id) {
        Optional<Author> author = authorRepo.findById(id);
        if(author.isEmpty()) {
            return "Author not found";
        }
        authorRepo.delete(author.get());
        return "Author deleted";

    }

    @Override
    public String listAuthors() {
        List<Author> genres = authorRepo.findAll();
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
