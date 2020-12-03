package ru.otus.work1.dao;

import ru.otus.work1.domain.Question;

public interface QuestionDao {
    Question getNextQuestion();
    void reset();
}
