package ru.otus.work5.dao;

import ru.otus.work5.domain.Question;

public interface QuestionDao {
    Question getNextQuestion();
    void restart();
}
