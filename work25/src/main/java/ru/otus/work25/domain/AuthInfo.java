package ru.otus.work25.domain;

import lombok.Data;

import java.util.List;

@Data
public class AuthInfo {
    private boolean authenticated;
    private String username;
    private List<String> authorities;
}
