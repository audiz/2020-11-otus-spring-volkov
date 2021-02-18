package ru.otus.work17.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "BOOKS")
public class Book {
    @Transient
    public static final String SEQUENCE_NAME = "book";

    @Id
    private long id;

    private String title;

    private Genre genre;

    private Author author;

    @JsonIgnore
    @ToString.Exclude
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonProperty("commentsSize")
    public int commentsSize() {
        return comments.size();
    }
}