package ru.otus.work9.service;

public interface BookService {
    String insertBook(Long id, String title, Long genre, Long author);
    String updateBook(Long id, String title, Long genre, Long author);
    String deleteBook(Long id);
    String listBooks();
    String getBook(Long id);
}
