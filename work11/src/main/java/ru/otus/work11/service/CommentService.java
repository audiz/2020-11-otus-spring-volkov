package ru.otus.work11.service;

public interface CommentService {
    String listComments(Long bookId);
    String insertComment(Long bookId, String comment);
    String updateComment(Long id, String comment);
    String deleteComment(Long id);
}
