package ru.otus.work1.domain;

public class Answer {
    private final static String USER_INPUT_PATTERN = "#USER_INPUT#";
    private final String answer;
    private final int number;
    private final boolean isCorrect;
    private final String correctAnswerStr;
    private boolean isAnswered;

    public Answer(String answer, int num, String str) {
        this.answer = answer;
        this.number = num;
        if(USER_INPUT_PATTERN.equals(answer)) {
            this.correctAnswerStr = str;
            this.isCorrect = true;
        } else {
            this.correctAnswerStr = null;
            this.isCorrect = convertToBoolean(str);
        }
    }

    public int getNumber() {
        return number;
    }

    public boolean isUserInput() {
        return USER_INPUT_PATTERN.equals(answer);
    }

    public boolean isCorrect(){
        return isCorrect;
    }

    public String getCorrectStr() {
        return correctAnswerStr;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswered(boolean answeredCorrect) {
        this.isAnswered = answeredCorrect;
    }

    public boolean isAnswerd() {
        return isAnswered;
    }

    private boolean convertToBoolean(String value) {
        return "1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                ", number=" + number +
                ", isCorrect=" + isCorrect +
                ", correctAnswerStr='" + correctAnswerStr + '\'' +
                ", isAnswered=" + isAnswered +
                '}';
    }
}