package ru.otus.work23.domain;

import lombok.Data;

@Data
public class AuthInfo {
    private boolean authenticated;
    private String username;
}
