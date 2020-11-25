package ru.otus.work1.service;

import ru.otus.work1.dao.QuestionDao;
import ru.otus.work1.domain.Question;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;
    private int questionNumber;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public Question getNextQuestion() {
        Question question = dao.getNextQuestion();
        if(question == null) {
            return null;
        }
        question.setNumber(++questionNumber);
        return question;
    }

    @Override
    public void reset() {
        questionNumber = 0;
        dao.reset();
    }
}
