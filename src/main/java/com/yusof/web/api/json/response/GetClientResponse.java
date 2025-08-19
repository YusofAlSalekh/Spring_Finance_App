package com.yusof.web.api.json.response;

import com.yusof.web.service.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetClientResponse {
    private ClientDTO clientDTO;
}
