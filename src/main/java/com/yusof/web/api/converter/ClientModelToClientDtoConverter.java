package com.yusof.web.api.converter;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.service.ClientDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ClientModelToClientDtoConverter implements Converter<ClientModel, ClientDTO> {

    @Override
    public ClientDTO convert(ClientModel source) {
        return new ClientDTO(
                source.getId(),
                source.getEmail());
    }
}