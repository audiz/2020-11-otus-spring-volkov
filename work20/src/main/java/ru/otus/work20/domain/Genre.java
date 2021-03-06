package ru.otus.work20.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.work20.events.UpdateBookCascade;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "GENRES")
@UpdateBookCascade(value = "genre")
public class Genre {
    @Transient
    public static final String SEQUENCE_NAME = "genre";

    @Id
    private Long id;

    private String name;
}
