package ru.otus.work16.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.work16.domain.Book;
import ru.otus.work16.domain.Comment;
import ru.otus.work16.domain.Genre;
import ru.otus.work16.repositories.BookRepository;
import ru.otus.work16.repositories.CommentRepository;
import ru.otus.work16.repositories.GenreRepository;
import ru.otus.work16.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class GenreController {

    private final GenreRepository genreRepository;
    private final SequenceGeneratorService service;

    @GetMapping("/genres")
    public String listGenres(Model model) {
        List<Genre> genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        return "listGenres";
    }

    @GetMapping("/genres/{id}/edit")
    public String editGenre(@PathVariable("id") long id, Model model) {
        var genre = genreRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("genre", genre);
        return "editGenre";
    }

    @GetMapping("/genres/new")
    public String newGenre(Model model) {
        model.addAttribute("genre", new Genre());
        return "editGenre";
    }


    @RequestMapping(value = "/genres/save", method= RequestMethod.POST)
    public String saveGenre(@ModelAttribute(value="genre") Genre genre) {
        if(genre.getId() == 0) {
            genre.setId(service.getSequenceNumber(Genre.SEQUENCE_NAME));
        }
        genreRepository.save(genre);
        return "redirect:/genres";
    }


    @GetMapping("/genres/{id}/delete")
    public String deleteGenre(@PathVariable("id") long id, Model model) {
        var genre = genreRepository.findById(id).orElseThrow(NotFoundException::new);
        genreRepository.delete(genre);
        return "redirect:/genres";
    }

}
