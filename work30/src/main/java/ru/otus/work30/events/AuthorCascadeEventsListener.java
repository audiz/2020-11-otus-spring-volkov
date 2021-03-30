package ru.otus.work30.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.work30.domain.Author;
import ru.otus.work30.domain.Genre;
import ru.otus.work30.repositories.BookRepository;
import ru.otus.work30.service.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class AuthorCascadeEventsListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        var exists = bookRepository.isExistsByAuthorId(id);
        if(exists) {
            throw new RuntimeException("Book with author exists");
        }
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Author> event) {
        if(event.getSource().getId() == 0) {
            event.getDocument().put("_id", service.getSequenceNumber(Author.SEQUENCE_NAME));
        }
        super.onBeforeSave(event);
    }

}

