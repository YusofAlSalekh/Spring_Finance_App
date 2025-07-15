package ru.yusof.json.request;

import lombok.Data;

@Data
public class AuthorizationRequest {
    private String email;
    private String password;
}
