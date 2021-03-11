package ru.otus.work25.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "USERS")
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "user";

    @Id
    private Long id;

    private String username;

    private String password;

    private String[] roles;
}