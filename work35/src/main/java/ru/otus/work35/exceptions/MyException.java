package ru.otus.work35.exceptions;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {
    private String errorMessage;

    public MyException() {

    }

    public MyException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MyException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
