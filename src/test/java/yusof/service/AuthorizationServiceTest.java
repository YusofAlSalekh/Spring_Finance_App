package yusof.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import yusof.converter.UserModelToUserDtoConverter;
import yusof.dao.UserDao;
import yusof.dao.UserModel;
import yusof.exceptions.AlreadyExistsException;
import yusof.exceptions.CustomException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @InjectMocks
    AuthorizationService subj;
    @Mock
    UserDao userDao;
    @Mock
    DigestService digestService;
    @Mock
    UserModelToUserDtoConverter userDtoConverter;

    @Test
    public void authorize_userNotFound() {
        when(digestService.hex("password")).thenReturn("hex");
        when(userDao.findByEmailAndHash("yus060571@gmail.com", "hex")).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> {
            subj.authorize("yus060571@gmail.com", "password");
        }, "incorrect login or password");

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("yus060571@gmail.com", "hex");
        verifyNoInteractions(userDtoConverter);
    }

    @Test
    public void authorize_userFound() {
        when(digestService.hex("password")).thenReturn("hex");

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("yus060571@gmail.com");
        userModel.setPassword("hex");
        when(userDao.findByEmailAndHash("yus060571@gmail.com", "hex")).thenReturn(Optional.of(userModel));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("yus060571@gmail.com");
        when(userDtoConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.authorize("yus060571@gmail.com", "password");
        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("yus060571@gmail.com", "hex");
        verify(userDtoConverter, times(1)).convert(userModel);
    }

    @Test
    void register_userAlreadyExists() {
        when(digestService.hex("password")).thenReturn("hex");
        when(userDao.insert("yus060571@gmail.com", "hex")).thenThrow(new AlreadyExistsException("User with the given email already exists."));

        assertThrows(AlreadyExistsException.class, () -> {
            subj.register("yus060571@gmail.com", "password");
        }, "User with the given email already exists.");

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).insert("yus060571@gmail.com", "hex");
        verifyNoInteractions(userDtoConverter);
    }

    @Test
    void register_userAdded() {
        when(digestService.hex("password")).thenReturn("hex");

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setPassword("hex");
        userModel.setEmail("yus060571@gmail.com");
        when(userDao.insert("yus060571@gmail.com", "hex")).thenReturn(userModel);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("yus060571@gmail.com");
        when(userDtoConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.register("yus060571@gmail.com", "password");
        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).insert("yus060571@gmail.com", "hex");
        verify(userDtoConverter, times(1)).convert(userModel);
    }

    @Test
    void register_userDidNotAdded() {
        when(digestService.hex("password")).thenReturn("hex");
        when(userDao.insert("yus060571@gmail.com", "hex")).thenThrow(new CustomException("Something went wrong during registration. Please, try again later"));

        assertThrows(CustomException.class, () -> {
            subj.register("yus060571@gmail.com", "password");
        }, "Something went wrong during registration. Please, try again later");

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).insert("yus060571@gmail.com", "hex");
        verifyNoInteractions(userDtoConverter);
    }
}





