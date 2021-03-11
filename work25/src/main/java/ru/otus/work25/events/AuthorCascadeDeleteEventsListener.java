package ru.otus.work25.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.work25.domain.Author;
import ru.otus.work25.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class AuthorCascadeDeleteEventsListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        var exists = bookRepository.isExistsByAuthorId(id);
        if(exists) {
            throw new RuntimeException("Book with author exists");
        }
    }

}

