package ru.yusof.json.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String login;
    private String password;
}
