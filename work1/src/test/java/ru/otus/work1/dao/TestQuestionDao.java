package ru.otus.work1.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.work1.domain.Question;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionDaoImpl")
public class TestQuestionDao {

    @DisplayName("корректно читает файл")
    @Test
    void shouldReadFileLine() {
        QuestionDaoImpl dao = new QuestionDaoImpl("/questions.csv", ";");
        Question question = dao.getNextQuestion();

        assertNotNull(question);
    }
}
