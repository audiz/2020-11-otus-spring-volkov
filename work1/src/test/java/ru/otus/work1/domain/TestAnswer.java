package ru.otus.work1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Answer")
public class TestAnswer {

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Answer answer = new Answer("Ответ", 1);
        assertEquals(answer.getAnswer(), "Ответ");
        assertEquals(answer.getNumber(), 1);
    }
}
