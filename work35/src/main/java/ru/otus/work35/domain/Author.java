package ru.otus.work35.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.work35.events.UpdateBookCascade;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "AUTHORS")
@UpdateBookCascade(value = "author")
public class Author {
    @Transient
    public static final String SEQUENCE_NAME = "author";

    @Id
    private long id;

    private String name;
}
