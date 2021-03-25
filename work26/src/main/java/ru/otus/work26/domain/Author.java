package ru.otus.work26.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "AUTHORS")
public class Author {
    @Transient
    public static final String SEQUENCE_NAME = "author";

    @Id
    private long id;

    private String name;
}
