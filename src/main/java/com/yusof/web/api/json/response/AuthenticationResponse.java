package com.yusof.web.api.json.response;

import com.yusof.web.service.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private Integer id;
    private String email;

    public static AuthenticationResponse fromDTO(ClientDTO clientDTO) {
        return new AuthenticationResponse(clientDTO.getId(), clientDTO.getEmail());
    }
}
