package ru.otus.work9.service;

public interface GenreService {
    String insertGenre(Long id, String name);
    String updateGenre(Long id, String name);
    String deleteGenre(Long id);
    String listGenres();
    String getGenre(Long id);
}
