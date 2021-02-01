package ru.otus.work16.rest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e, Model model) {
        model.addAttribute("exception", e);
        return "error";
    }
}