package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDeletionRequest {
    @NotNull
    @Positive(message = "A positive number must be provided")
    private Integer accountId;
}
