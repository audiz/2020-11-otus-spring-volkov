package ru.otus.work9.service;

public interface CommentService {
    String listComments(Long id);
    String insertComment(Long bookId, String comment);
    String updateComment(Long id, String comment);
    String deleteComment(Long id);
}
