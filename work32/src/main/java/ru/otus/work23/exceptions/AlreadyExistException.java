package ru.otus.work23.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistException extends MyException {

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
