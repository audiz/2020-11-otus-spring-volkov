package ru.otus.work35.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.otus.work35.domain.Book;
import ru.otus.work35.domain.Comment;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Component
public class CascadeOperationsMongoEventListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if ((source instanceof Comment)) {
            handleComment(source);
        }
        if(source.getClass().isAnnotationPresent(UpdateBookCascade.class)) {
            ReflectionUtils.doWithFields(source.getClass(), new CascadeCallback(source, mongoOperations));
        }
    }

    private void handleComment(Object source) {
        Comment comment = ((Comment) source);
        Book book = comment.getBook();
        mergeComment(book.getComments(), comment);
        mongoOperations.save(book);
    }

    private void mergeComment(List<Comment> list, Comment commentNew) {
        int index = 0;
        for(Comment comment : list) {
            if(comment.getId() == commentNew.getId()) {
                list.set(index, commentNew);
                return;
            }
            index++;
        }
        list.add(commentNew);
    }

}