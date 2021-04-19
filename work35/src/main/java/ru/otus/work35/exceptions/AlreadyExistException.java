package ru.otus.work35.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistException extends MyException {

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
