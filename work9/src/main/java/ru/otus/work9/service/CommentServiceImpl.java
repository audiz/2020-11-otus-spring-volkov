package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.work9.domain.Book;
import ru.otus.work9.domain.Comment;
import ru.otus.work9.domain.Genre;
import ru.otus.work9.repositories.BookRepository;
import ru.otus.work9.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public String listComments(Long id) {
        List<Comment> genres = commentRepository.getAllByBookId(id);
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "COMMENT");
        at.addRule();
        genres.forEach(genre -> {
            at.addRow(genre.getId(), genre.getComment());
            at.addRule();
        });
        return at.render();
    }

    @Override
    public String insertComment(Long bookId, String comment) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()){
            return "Book not found";
        }
        commentRepository.insert(new Comment(0, comment, optionalBook.get()));
        return "Success";
    }

    @Override
    public String updateComment(Long id, String comment) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isEmpty()) {
            return "Comment not found";
        }
        optionalComment.get().setComment(comment);
        commentRepository.update(optionalComment.get());
        return "Comment updated";
    }

    @Override
    public String deleteComment(Long id) {
        if(commentRepository.findById(id).isEmpty()) {
            return "Comment not found";
        }
        int result = commentRepository.deleteById(id);
        if(result == 1) {
            return "Comment deleted";
        }
        return "Can't delete";
    }
}
