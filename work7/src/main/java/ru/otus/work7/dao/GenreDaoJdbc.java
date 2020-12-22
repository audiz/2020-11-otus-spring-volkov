package ru.otus.work7.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Genre;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations)
    {
        this.jdbc = jdbcOperations;
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.getJdbcOperations().query("select `id`, `name` from genres", new GenreMapper());
    }

    @Override
    public void insert(Genre genre) {
        jdbc.update("insert into genres(`id`, `name`) values(:id, :name)", Map.of("id", genre.getId(), "name", genre.getName()));
    }

    @Override
    public Genre getById(long id) {
        try {
            return jdbc.queryForObject("select `id`, `name` from genres where `id` = :id", Map.of("id", id), new GenreMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void update(Genre genre) {
        jdbc.update("update genres set `name` = :name where `id` = :id", Map.of("id", genre.getId(), "name", genre.getName()));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from genres where `id` = :id", Map.of("id", id));
    }
}
