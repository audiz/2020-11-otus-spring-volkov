package ru.otus.work25.repositories;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.work25.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    @ExistsQuery(value="{ 'name' : {$regex : /^:#{#name}$/, $options: 'i'} }")
    boolean isExistsByNameRegexIgnoreCase(@Param("name") String name);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    Genre save(Genre genre);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void deleteById(Long aLong);
}