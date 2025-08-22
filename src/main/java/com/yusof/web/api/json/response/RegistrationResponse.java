package com.yusof.web.api.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationResponse {
    private int id;
    private String email;
}
