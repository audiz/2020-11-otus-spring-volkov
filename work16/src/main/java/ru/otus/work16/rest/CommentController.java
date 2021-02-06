package ru.otus.work16.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.work16.domain.Book;
import ru.otus.work16.domain.Comment;
import ru.otus.work16.repositories.AuthorRepository;
import ru.otus.work16.repositories.BookRepository;
import ru.otus.work16.repositories.CommentRepository;
import ru.otus.work16.repositories.GenreRepository;
import ru.otus.work16.service.SequenceGeneratorService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final SequenceGeneratorService service;

    @RequestMapping(value = "/comments/save", method= RequestMethod.POST)
    public String saveComment(@RequestParam(value = "bookId", required = false) long bookId,
                              @RequestParam(value = "comment", required = false) String comment) {
        var optionalBook = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        commentRepository.save(new Comment(service.getSequenceNumber(Comment.SEQUENCE_NAME), comment, optionalBook));
        return "redirect:/books/" + bookId;
    }

}
