package ru.otus.work9.service;

public interface AuthorService {
    String insertAuthor(Long id, String name);
    String updateAuthor(Long id, String name);
    String deleteAuthor(Long id);
    String listAuthors();
    String getAuthor(Long id);
}
