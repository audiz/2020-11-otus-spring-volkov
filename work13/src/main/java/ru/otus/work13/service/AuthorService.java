package ru.otus.work13.service;

public interface AuthorService {
    String insertAuthor(String name);
    String updateAuthor(Long id, String name);
    String deleteAuthor(Long id);
    String listAuthors();
    String getAuthor(Long id);
}
