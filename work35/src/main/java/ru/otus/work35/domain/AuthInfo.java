package ru.otus.work35.domain;

import lombok.Data;

@Data
public class AuthInfo {
    private boolean authenticated;
    private String username;
}
