package ru.otus.work23.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "COMMENTS")
public class Comment {
    @Transient
    public static final String SEQUENCE_NAME = "comment";

    @Id
    private long id;

    private String comment;

    @JsonIgnore
    @ToString.Exclude
    @DBRef
    private Book book;
}
