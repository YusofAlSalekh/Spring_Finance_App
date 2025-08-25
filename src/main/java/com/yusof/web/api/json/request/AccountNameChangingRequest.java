package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AccountNameChangingRequest {
    @NotBlank
    private String name;

    @NotNull
    @Positive(message = "Account id must be a positive number")
    private Integer accountId;
}
