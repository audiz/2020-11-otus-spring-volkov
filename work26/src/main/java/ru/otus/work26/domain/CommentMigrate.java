package ru.otus.work26.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentMigrate {
    private String id;

    private String comment;

    private String bookId;
}
