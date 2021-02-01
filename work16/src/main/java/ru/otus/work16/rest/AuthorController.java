package ru.otus.work16.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.work16.domain.Author;
import ru.otus.work16.repositories.AuthorRepository;
import ru.otus.work16.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final SequenceGeneratorService service;

    @GetMapping("/authors")
    public String listGenres(Model model) {
        List<Author> genres = authorRepository.findAll();
        model.addAttribute("authors", genres);
        return "listAuthors";
    }

    @GetMapping("/authors/{id}/edit")
    public String editGenre(@PathVariable("id") long id, Model model) {
        var author = authorRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "editAuthor";
    }

    @GetMapping("/authors/new")
    public String newGenre(Model model) {
        model.addAttribute("author", new Author());
        return "editAuthor";
    }


    @RequestMapping(value = "/authors/save", method= RequestMethod.POST)
    public String saveGenre(@ModelAttribute(value="author") Author author) {
        if(author.getId() == 0) {
            author.setId(service.getSequenceNumber(Author.SEQUENCE_NAME));
        }
        authorRepository.save(author);
        return "redirect:/authors";
    }


    @GetMapping("/authors/{id}/delete")
    public String deleteGenre(@PathVariable("id") long id, Model model) {
        var author = authorRepository.findById(id).orElseThrow(NotFoundException::new);
        authorRepository.delete(author);
        return "redirect:/authors";
    }

}
