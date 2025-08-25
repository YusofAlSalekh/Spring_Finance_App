package com.yusof.web.service;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.BadCredentialsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.ClientRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final Converter<ClientModel, ClientDTO> clientDtoConverter;
    private int clientId;

    public AuthenticationService(ClientRepository clientRepository, Converter<ClientModel, ClientDTO> clientDtoConverter) {
        this.clientRepository = clientRepository;
        this.clientDtoConverter = clientDtoConverter;
    }

    public ClientDTO getClientById(Integer clientId) {
        ClientModel clientModel = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException("User with such id not found"));
        return clientDtoConverter.convert(clientModel);
    }

    public ClientDTO authorize(@Email @NotBlank String email, @NotBlank String password) {
        ClientModel clientModel = clientRepository.findByEmailAndPassword(
                        email,
                        DigestUtils.md5Hex(password))
                .orElseThrow(() -> new BadCredentialsException("Incorrect login or password"));
        clientId = clientModel.getId();
        return clientDtoConverter.convert(clientModel);
    }

    @Transactional
    public ClientDTO register(@Email @NotBlank String email, @NotBlank String password) {
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