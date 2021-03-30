package ru.otus.work30.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.work30.domain.Genre;
import ru.otus.work30.repositories.BookRepository;
import ru.otus.work30.service.SequenceGeneratorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreCascadeEventsListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;
    private final SequenceGeneratorService service;

    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        var exists = bookRepository.isExistsByGenreId(id);
        if(exists) {
            throw new RuntimeException("Book with genre exists");
        }
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Genre> event) {
        if(event.getSource().getId() == 0) {
            event.getDocument().put("_id", service.getSequenceNumber(Genre.SEQUENCE_NAME));
        }
        super.onBeforeSave(event);
    }
}

