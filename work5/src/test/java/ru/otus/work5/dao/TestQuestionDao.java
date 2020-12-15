package ru.otus.work5.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.work5.domain.Question;
import ru.otus.work5.service.DataReader;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DisplayName("Класс QuestionDaoImpl")
public class TestQuestionDao {

    @Autowired
    private QuestionDao dao;

    @MockBean
    private DataReader dataReader;

    @DisplayName("корректно парсит строку с вариантами")
    @Test
    void shouldReadLine() {

        Mockito.when(dataReader.readLine()).thenReturn("Which of the following is not a Java features?;Dynamic;1;Architecture Neutral;1;Use of pointers;0;Object-oriented;0");
        Question question = dao.getNextQuestion();
        assertNotNull(question);
        assertEquals(4,  question.getAnswers().size());
        assertTrue(question.getAnswers().get(0).isCorrect());
        assertFalse(question.getAnswers().get(3).isCorrect());
    }

    @DisplayName("корректно парсит строку с пользовательским вводом")
    @Test
    void shouldReadUserLine() {
        Mockito.when(dataReader.readLine()).thenReturn("What is the return type of the hashCode() method in the Object class?;#USER_INPUT#;int");
        Question question = dao.getNextQuestion();

        assertNotNull(question);
        assertEquals(1,  question.getAnswers().size());
        assertTrue(question.getAnswers().get(0).isUserInput());
    }
}
