package ru.otus.work20.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.work20.domain.Comment;
import ru.otus.work20.repositories.BookRepository;
import ru.otus.work20.repositories.CommentRepository;

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
            bookRepository.findById(commentRepository.findById(id).map(comment -> comment.getBook().getId()))
                    .filter(book -> book.getComments().size() == 1 && book.getComments().get(0) == null)
                    .map(book -> {
                        book.setComments(new ArrayList<>());
                        return book;
                    })
                    .flatMap(bookRepository::save)
                    .subscribe();
        }
    }
}

