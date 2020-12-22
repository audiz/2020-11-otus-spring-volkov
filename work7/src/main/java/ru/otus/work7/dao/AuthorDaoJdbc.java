package ru.otus.work7.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.work7.domain.Author;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations)
    {
        this.jdbc = jdbcOperations;
    }

    @Override
    public List<Author> getAll() {
        return jdbc.getJdbcOperations().query("select `id`, `name` from authors", new AuthorMapper());
    }

    @Override
    public void insert(Author author) {
        jdbc.update("insert into authors(`id`, `name`) values(:id, :name)", Map.of("id", author.getId(), "name", author.getName()));
    }

    @Override
    public Author getById(long id) {
        try {
            return jdbc.queryForObject("select `id`, `name` from authors where `id` = :id", Map.of("id", id), new AuthorMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void update(Author author) {
        jdbc.update("update authors set `name` = :name where `id` = :id", Map.of("id", author.getId(), "name", author.getName()));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from authors where `id` = :id", Map.of("id", id));
    }
}
