package ru.otus.work5.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс Question")
public class TestQuestion {

    @DisplayName("корректно считается процент ответов")
    @Test
    void shouldHaveCorrectConstructor() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("ответ 1", 1, "1"));
        answers.add(new Answer("ответ 2", 2, "1"));
        answers.get(0).setAnswered(true);
        Question question = new Question("Вопрос", answers);
        assertEquals(50.0, question.getCorrectPercents());
    }
}
