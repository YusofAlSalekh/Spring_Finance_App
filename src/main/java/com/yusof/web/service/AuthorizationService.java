package com.yusof.web.service;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.BadCredentialsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.ClientRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationService {
    private final ClientRepository clientRepository;
    private final Converter<ClientModel, ClientDTO> clientDtoConverter;
    private int clientId;

    public AuthorizationService(ClientRepository clientRepository, Converter<ClientModel, ClientDTO> clientDtoConverter) {
        this.clientRepository = clientRepository;
        this.clientDtoConverter = clientDtoConverter;
    }

    public ClientDTO getClientById(Integer clientId) {
        ClientModel clientModel = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException("User with such id not found"));
        return clientDtoConverter.convert(clientModel);
    }

    public ClientDTO authorize(String email, String password) {
        ClientModel clientModel = clientRepository.findByEmailAndPassword(
                        email,
                        DigestUtils.md5Hex(password))
                .orElseThrow(() -> new BadCredentialsException("Incorrect login or password"));
        clientId = clientModel.getId();
        return clientDtoConverter.convert(clientModel);
    }

    @Transactional
    public ClientDTO register(String email, String password) {
        try {
            ClientModel clientModel = new ClientModel();
            clientModel.setEmail(email);
            clientModel.setPassword(DigestUtils.md5Hex(password));

            clientRepository.save(clientModel);
            return clientDtoConverter.convert(clientModel);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Client with the given email already exists.");
        }
    }

    public int getClientId() {
        return clientId;
    }
}