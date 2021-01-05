package ru.otus.work9.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.work9.domain.Book;
import ru.otus.work9.domain.Comment;
import ru.otus.work9.repositories.BookRepository;
import ru.otus.work9.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public String listComments(Long bookId) {
        List<Comment> comments;
        if(bookId == 0) {
            comments = commentRepository.getAll();
        } else {
            Optional<Book> optionalBook = bookRepository.getFullById(bookId);
            if(optionalBook.isEmpty()){
                return "Not found";
            }
            comments = optionalBook.get().getComments();
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "COMMENT");
        at.addRule();
        comments.forEach(genre -> {
            at.addRow(genre.getId(), genre.getComment());
            at.addRule();
        });
        return at.render();
    }

    @Transactional
    @Override
    public String insertComment(Long bookId, String comment) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()){
            return "Book not found";
        }
        commentRepository.insert(new Comment(0, comment, optionalBook.get()));
        return "Success";
    }

    @Transactional
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

    @Transactional
    @Override
    public String deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()) {
            return "Comment not found";
        }
        commentRepository.delete(comment.get());
        return "Comment deleted";
    }
}
