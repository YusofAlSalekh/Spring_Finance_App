package com.yusof.web.service;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.BadCredentialsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.ClientRepository;
import com.yusof.web.security.ClientRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final Converter<ClientModel, ClientDTO> clientDtoConverter;
    private final PasswordEncoder passwordEncoder;

    public ClientDTO getClientById(Integer clientId) {
        ClientModel clientModel = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException("User with such id not found"));
        return clientDtoConverter.convert(clientModel);
    }

    public ClientDTO authorize(@Email @NotBlank String email, @NotBlank String rawPassword) {
        ClientModel clientModel = clientRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Incorrect login"));

        if (!passwordEncoder.matches(rawPassword, clientModel.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        return clientDtoConverter.convert(clientModel);
    }

    @Transactional
    public ClientDTO register(@Email @NotBlank String email, @NotBlank String password) {
        try {
            ClientModel clientModel = new ClientModel();
            clientModel.setEmail(email);
            clientModel.setPassword(passwordEncoder.encode(password));
            clientModel.getRoles().add(ClientRole.USER);

            clientRepository.save(clientModel);
            return clientDtoConverter.convert(clientModel);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Client with the given email already exists.");
        }
    }
}