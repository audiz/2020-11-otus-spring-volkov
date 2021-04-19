package ru.otus.work35.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class Summary implements Serializable {
    private long books;
    private long authors;
    private long genres;
    private long comments;
}
