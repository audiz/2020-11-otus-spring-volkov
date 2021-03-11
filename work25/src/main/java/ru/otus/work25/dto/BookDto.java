package ru.otus.work25.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.work25.domain.Book;

@AllArgsConstructor
@Data
public class BookDto {
    private Book book;
    private boolean hasReadPermission;
    private boolean hasWritePermission;
}
