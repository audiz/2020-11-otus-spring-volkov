package ru.otus.work17.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.work17.domain.Genre;
import ru.otus.work17.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class GenreCascadeDeleteEventsListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;

    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        var exists = bookRepository.isExistsByGenreId(id);
        if(exists) {
            throw new RuntimeException("Book with genre exists");
        }
    }

}

