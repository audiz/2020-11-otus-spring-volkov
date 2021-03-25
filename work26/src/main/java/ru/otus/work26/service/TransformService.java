package ru.otus.work26.service;

import org.springframework.stereotype.Service;
import ru.otus.work26.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransformService {

    private Map<Long, String> authorUuidMap = new HashMap<>();
    private Map<Long, String> genreUuidMap = new HashMap<>();
    private Map<Long, String> bookUuidMap = new HashMap<>();

    public AuthorMigrate transformAuthor(Author author) {
        String uuid = UUID.randomUUID().toString();
        authorUuidMap.put(author.getId(), uuid);
        return new AuthorMigrate(uuid, author.getName());
    }

    public GenreMigrate transformGenre(Genre genre) {
        String uuid = UUID.randomUUID().toString();
        genreUuidMap.put(genre.getId(), uuid);
        return new GenreMigrate(uuid, genre.getName());
    }

    public BookMigrate transformBook(Book book) {
        String uuid = UUID.randomUUID().toString();
        bookUuidMap.put(book.getId(), uuid);
        return new BookMigrate(uuid, book.getTitle(), genreUuidMap.get(book.getGenre().getId()), authorUuidMap.get(book.getAuthor().getId()));
    }

    public CommentMigrate transformComment(Comment comment) {
        String uuid = UUID.randomUUID().toString();
        authorUuidMap.put(comment.getId(), uuid);
        return new CommentMigrate(uuid, comment.getComment(), bookUuidMap.get(comment.getBook().getId()));
    }
}
