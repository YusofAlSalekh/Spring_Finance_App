package com.yusof.web.api.json.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequest {
    @Email
    @NotBlank(message = "Email must be provided")
    private String login;

    @NotBlank(message = "Password must be provided")
    private String password;
}
