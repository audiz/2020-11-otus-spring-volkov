package ru.otus.work30.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.work30.domain.Author;
import ru.otus.work30.domain.Book;
import ru.otus.work30.repositories.CommentRepository;
import ru.otus.work30.service.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class BookCascadeEventsListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;
    private final SequenceGeneratorService service;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        commentRepository.deleteAllByBookId(id);
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Book> event) {
        if(event.getSource().getId() == 0) {
            event.getDocument().put("_id", service.getSequenceNumber(Book.SEQUENCE_NAME));
        }
        super.onBeforeSave(event);
    }
}

