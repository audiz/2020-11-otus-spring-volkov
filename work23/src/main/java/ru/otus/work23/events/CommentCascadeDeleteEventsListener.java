package ru.otus.work23.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.work23.domain.Comment;
import ru.otus.work23.repositories.BookRepository;
import ru.otus.work23.repositories.CommentRepository;

import java.util.ArrayList;

@Log4j2
@Component
@RequiredArgsConstructor
public class CommentCascadeDeleteEventsListener extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public void onBeforeDelete(BeforeDeleteEvent<Comment> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        val id = source.get("_id", Long.class);
        if(id != null) {
            val comment = commentRepository.findById(id);
            comment.ifPresent(comment1 -> {
                val book = bookRepository.findById(comment1.getBook().getId());
                book.ifPresent(book1 -> {
                    if(book1.getComments().size() == 1 && book1.getComments().get(0).getId() == id) {
                        book1.setComments(new ArrayList<>());
                        bookRepository.save(book1);
                    }
                });
            });
        }
    }
}

