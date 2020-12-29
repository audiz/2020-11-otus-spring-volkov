package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Genre;
import ru.otus.work9.repositories.BookRepository;
import ru.otus.work9.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GenreServiceImpl implements GenreService {

    private final BookRepository bookRepo;
    private final GenreRepository genreRepo;

    @Override
    public String insertGenre(String name) {
        genreRepo.insert(new Genre(0, name));
        return "Success";
    }

    @Override
    public String updateGenre(Long id, String name) {
        if(genreRepo.findById(id).isEmpty()) {
            return "Genre not found";
        }
        genreRepo.update(new Genre(id, name));
        return "Genre updated";
    }

    @Override
    public String deleteGenre(Long id) {
        if(bookRepo.getCountGenreById(id) > 0) {
            return "You can't delete an genre because books with it exist";
        }
        if(genreRepo.findById(id).isEmpty()) {
            return "Genre not found";
        }
        int result = genreRepo.deleteById(id);
        if(result == 1) {
            return "Genre deleted";
        }
        return "Can't delete";
    }

    @Override
    public String listGenres() {
        List<Genre> genres = genreRepo.getAll();
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
    public String getGenre(Long id) {
        Optional<Genre> genre = genreRepo.findById(id);
        if(genre.isEmpty()){
            return "Genre not found";
        } else {
            return genre.get().toString();
        }
    }

}
