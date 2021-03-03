package ru.otus.work23.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.work23.domain.Book;

public interface BookRepository extends MongoRepository<Book, Long> {
    @ExistsQuery(value="{ 'genre._id' : :#{#genreId} }")
    boolean isExistsByGenreId(@Param("genreId")  long genreId);
    @ExistsQuery(value="{ 'author._id' : :#{#authorId} }")
    boolean isExistsByAuthorId(@Param("authorId")  long authorId);
    @ExistsQuery(value="{ 'title' : {$regex : /^:#{#title}$/, $options: 'i'} }")
    boolean isExistsByTitleRegexIgnoreCase(@Param("title") String title);
}