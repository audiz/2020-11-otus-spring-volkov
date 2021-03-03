package ru.otus.work20.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Summary {
    private long books;
    private long authors;
    private long genres;
    private long comments;
}
