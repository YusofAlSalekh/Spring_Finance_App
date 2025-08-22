package com.yusof.web.service;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.BadCredentialsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.ClientRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @InjectMocks AuthorizationService subject;

    @Mock
    ClientRepository clientRepository;

    @Mock
    Converter<ClientModel, ClientDTO> clientDtoConverter;


    @Test
    void getClientById_success() {
        Integer id = 1;

        ClientModel model = new ClientModel();
        model.setId(id);
        model.setEmail("user@gmail.com");

        ClientDTO dto = new ClientDTO();
        dto.setId(id);
        dto.setEmail("user@site.com");

        when(clientRepository.findById(id)).thenReturn(Optional.of(model));
        when(clientDtoConverter.convert(model)).thenReturn(dto);

        ClientDTO result = subject.getClientById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("user@site.com", result.getEmail());

        verify(clientRepository).findById(id);
        verify(clientDtoConverter).convert(model);
        verifyNoMoreInteractions(clientRepository, clientDtoConverter);
    }

    @Test
    void getClientById_throwsNotFoundException() {
        Integer id = 1;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrowsExactly(NotFoundException.class, () -> subject.getClientById(id));

        verify(clientRepository).findById(id);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(clientDtoConverter);
    }

    @Test
    void authorize_success() {
        String email = "user@gmail.com";
        String password = "password";
        String hash = DigestUtils.md5Hex(password);

        ClientModel model = new ClientModel();
        model.setId(1);
        model.setEmail(email);
        model.setPassword(hash);

        ClientDTO dto = new ClientDTO();
        dto.setId(1);
        dto.setEmail(email);

        when(clientRepository.findByEmailAndPassword(email, hash)).thenReturn(Optional.of(model));
        when(clientDtoConverter.convert(model)).thenReturn(dto);

        ClientDTO result = subject.authorize(email, password);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, subject.getClientId());
        assertEquals(email, result.getEmail());

        verify(clientRepository).findByEmailAndPassword(email, hash);
        verify(clientDtoConverter).convert(model);
        verifyNoMoreInteractions(clientRepository, clientDtoConverter);
    }

    @Test
    void authorize_throwsBadCredentialsException() {
        String email = "user@gmail.com";
        String password = "password";
        String hash = DigestUtils.md5Hex(password);

        when(clientRepository.findByEmailAndPassword(email, hash)).thenReturn(Optional.empty());

        assertThrowsExactly(BadCredentialsException.class, () -> subject.authorize(email, password));

        verify(clientRepository).findByEmailAndPassword(email, hash);
        verifyNoMoreInteractions(clientRepository);
        verify(clientDtoConverter, never()).convert(any());
    }

    @Test
    void register_success() {
        String email = "user@gmail.com";
        String password = "password";
        String hash = DigestUtils.md5Hex(password);

        ClientDTO dto = new ClientDTO();
        dto.setEmail(email);

        when(clientDtoConverter.convert(any(ClientModel.class))).thenReturn(dto);

        ClientDTO result = subject.register(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        ArgumentCaptor<ClientModel> captor = ArgumentCaptor.forClass(ClientModel.class);
        verify(clientRepository).save(captor.capture());
        ClientModel saved = captor.getValue();
        assertEquals(email, saved.getEmail());
        assertEquals(hash, saved.getPassword());

        verify(clientDtoConverter).convert(saved);
        verifyNoMoreInteractions(clientRepository, clientDtoConverter);
    }

    @Test
    void register_throwsAlreadyExistsException() {
        String email = "user@gmail.com";
        String password = "password";

        doThrow(new DataIntegrityViolationException(""))
                .when(clientRepository).save(any(ClientModel.class));

        assertThrowsExactly(AlreadyExistsException.class, () -> subject.register(email, password));

        verify(clientRepository).save(any(ClientModel.class));
        verifyNoMoreInteractions(clientRepository);
        verify(clientDtoConverter, never()).convert(any(ClientModel.class));
    }
}