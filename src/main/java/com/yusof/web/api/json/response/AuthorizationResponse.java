package com.yusof.web.api.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationResponse {
    private int id;
    private String email;
}
