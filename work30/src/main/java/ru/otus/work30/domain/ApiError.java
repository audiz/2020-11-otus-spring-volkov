package ru.otus.work30.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

    private int code;
    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.code = status.value();
        this.status = status;
        this.message = message;
    }
}