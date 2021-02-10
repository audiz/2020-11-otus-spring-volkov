package ru.otus.work17.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistException extends MyException {

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
