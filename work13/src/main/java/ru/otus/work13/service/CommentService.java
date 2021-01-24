package ru.otus.work13.service;

public interface CommentService {
    String listComments(Long bookId);
    String insertComment(Long bookId, String comment);
    String updateComment(Long id, String comment);
    String deleteComment(Long id);
}
