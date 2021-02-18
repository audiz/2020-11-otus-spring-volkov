package ru.otus.work20.events;

import java.lang.reflect.Field;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;
import ru.otus.work20.domain.Book;

public class CascadeCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private ReactiveMongoOperations mongoOperations;

    public CascadeCallback(final Object source, final ReactiveMongoOperations mongoOperations) {
        this.source = source;
        this.setMongoOperations(mongoOperations);
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(Id.class)) {
            final Object fieldValue = field.get(getSource());
            if (fieldValue != null) {
                UpdateBookCascade annotation = source.getClass().getAnnotation(UpdateBookCascade.class);
                Query select = Query.query(Criteria.where(annotation.value() + "._id").is(fieldValue));
                Update update = new Update();
                update.set(annotation.value(), source);
                getMongoOperations().updateMulti(select, update, Book.class).subscribe();
            }
        }

    }

    private Object getSource() {
        return source;
    }

    public void setSource(final Object source) {
        this.source = source;
    }

    private ReactiveMongoOperations getMongoOperations() {
        return mongoOperations;
    }

    private void setMongoOperations(final ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
}
