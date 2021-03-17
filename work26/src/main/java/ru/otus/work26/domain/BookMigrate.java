package ru.otus.work26.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMigrate {
    private String id;

    private String title;

    private String genreId;

    private String authorId;
}