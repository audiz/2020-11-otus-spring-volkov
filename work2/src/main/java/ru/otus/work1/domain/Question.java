package ru.otus.work1.domain;

import java.util.List;

public class Question {
    private final String question;
    private final List<Answer> answers;
    private int number;

    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Double getCorrectPercents() {
        return answers.stream().filter(Answer::isCorrect).mapToDouble(answer -> answer.isAnswerd() ? 100 : 0).average().orElse(Double.NaN);
    }
}
