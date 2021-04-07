package ru.otus.work23.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends MyException {

    public NotFoundException() {
    }

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
