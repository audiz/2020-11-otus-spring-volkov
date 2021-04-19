package ru.otus.work35.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends MyException {

    public NotFoundException() {
    }

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
