package ru.otus.work1.service;

import ru.otus.work1.domain.Question;

public interface QuestionService {
    Question getNextQuestion();
    void reset();
}
