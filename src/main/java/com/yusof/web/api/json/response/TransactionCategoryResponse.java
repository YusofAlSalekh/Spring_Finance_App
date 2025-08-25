package com.yusof.web.api.json.response;

import com.yusof.web.service.TransactionCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCategoryResponse {
    private Integer id;
    private String name;
    private Integer clientId;

    public static TransactionCategoryResponse fromDTO(TransactionCategoryDTO dto) {
        return new TransactionCategoryResponse(dto.getId(), dto.getName(), dto.getClientId());
    }
}
