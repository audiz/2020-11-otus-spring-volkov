package ru.otus.work7.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private final long id;
    private String title;
    private Genre genre;
    private Author author;
}