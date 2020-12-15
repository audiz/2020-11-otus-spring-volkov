package ru.otus.work5.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.work5.domain.Question;
import ru.otus.work5.service.DataReader;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionDaoImpl")
public class TestQuestionDao {

    @DisplayName("корректно парсит строку с вариантами")
    @Test
    void shouldReadLine() {

        DataReader fileReader = new DataReader() {
            @Override
            public String readLine() {
                return "Which of the following is not a Java features?;Dynamic;1;Architecture Neutral;1;Use of pointers;0;Object-oriented;0";
            }

            @Override
            public void restart() {

            }
        };
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

        DataReader fileReader = new DataReader() {
            @Override
            public String readLine() {
                return "What is the return type of the hashCode() method in the Object class?;#USER_INPUT#;int";
            }

            @Override
            public void restart() {

            }
        };
        QuestionDaoImpl dao = new QuestionDaoImpl(fileReader, ";");
        Question question = dao.getNextQuestion();

        assertNotNull(question);
        assertEquals(1,  question.getAnswers().size());
        assertTrue(question.getAnswers().get(0).isUserInput());
    }
}
