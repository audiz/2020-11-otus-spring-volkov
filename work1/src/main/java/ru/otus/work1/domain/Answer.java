package ru.otus.work1.domain;

public class Answer {
    private final String answer;
    private final int number;

    public Answer(String answer, int num) {
        this.answer = answer;
        this.number = num;
    }

    public int getNumber() {
        return number;
    }

    public String getAnswer() {
        return answer;
    }
}