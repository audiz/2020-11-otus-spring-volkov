package ru.otus.work3.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.work3.domain.Question;
import ru.otus.work3.service.DataReader;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionDaoImpl")
public class TestQuestionDao {

    @DisplayName("корректно парсит строку с вариантами")
    @Test
    void shouldReadLine() {

        DataReader fileReader = () -> "Which of the following is not a Java features?;Dynamic;1;Architecture Neutral;1;Use of pointers;0;Object-oriented;0";
        QuestionDaoImpl dao = new QuestionDaoImpl(fileReader, ";");
        Question question = dao.getNextQuestion();

        assertNotNull(question);
        assertEquals(4,  question.getAnswers().size());
        assertTrue(question.getAnswers().get(0).isCorrect());
        assertFalse(question.getAnswers().get(3).isCorrect());
    }

    @DisplayName("корректно парсит строку с пользовательским вводом")
    @Test
    void shouldReadUserLine() {

        DataReader fileReader = () -> "What is the return type of the hashCode() method in the Object class?;#USER_INPUT#;int";
        QuestionDaoImpl dao = new QuestionDaoImpl(fileReader, ";");
        Question question = dao.getNextQuestion();

        assertNotNull(question);
        assertEquals(1,  question.getAnswers().size());
        assertTrue(question.getAnswers().get(0).isUserInput());
    }
}
