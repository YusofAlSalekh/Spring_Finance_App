package com.yusof.web.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationForm {
    @Email
    @NotBlank(message = "Email must be provided")
    private String login;

    @NotBlank(message = "Password must be provided")
    private String password;
}
