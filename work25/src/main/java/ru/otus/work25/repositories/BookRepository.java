package ru.otus.work25.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.work25.domain.Book;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
public interface BookRepository extends MongoRepository<Book, Long> {
    @ExistsQuery(value="{ 'genre._id' : :#{#genreId} }")
    boolean isExistsByGenreId(@Param("genreId")  long genreId);
    @ExistsQuery(value="{ 'author._id' : :#{#authorId} }")
    boolean isExistsByAuthorId(@Param("authorId")  long authorId);
    @ExistsQuery(value="{ 'title' : {$regex : /^:#{#title}$/, $options: 'i'} }")
    boolean isExistsByTitleRegexIgnoreCase(@Param("title") String title);

    // читать можно только по разрешениям acl
    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, admin)")
    @Override
    List<Book> findAll();

    // а изменять можно и просто с ROLE_ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#entity, 'write')")
    @Override
    Book save(Book entity);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(filterObject, 'delete')")
    @Override
    void deleteById(Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#entity, 'delete')")
    @Override
    void delete(Book entity);

    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @Override
    long count();
}