package ru.otus.work13.service;

public interface BookService {
    String insertBook(String title, Long genre, Long author);
    String updateBook(Long id, String title, Long genre, Long author);
    String deleteBook(Long id);
    String listBooks();
    String getBook(Long id);
}
