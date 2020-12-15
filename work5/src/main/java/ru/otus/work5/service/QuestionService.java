package ru.otus.work5.service;

import org.javatuples.Pair;
import org.springframework.lang.Nullable;
import ru.otus.work5.domain.Question;

public interface QuestionService {
    Pair<Question, String> getQuestion();
    boolean isAnsweredOk(Question question, String str);
    String prepareResults();
    boolean isAnswered();
    void restart();
    String getMessage(String bundle, @Nullable Object[] var);
}
