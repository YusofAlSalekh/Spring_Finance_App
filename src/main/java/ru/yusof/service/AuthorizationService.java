package ru.yusof.service;

import org.springframework.stereotype.Service;
import ru.yusof.converter.Converter;
import ru.yusof.dao.UserDao;
import ru.yusof.dao.UserModel;
import ru.yusof.exceptions.BadCredentialsException;

@Service
public class AuthorizationService {
    private final UserDao userDao;
    private final DigestService digestService;
    private final Converter<UserModel, UserDTO> userDtoConverter;
    private int clientId;

    public AuthorizationService(UserDao userDao, DigestService digestService, Converter<UserModel, UserDTO> userDtoConverter) {
        this.userDao = userDao;
        this.digestService = digestService;
        this.userDtoConverter = userDtoConverter;
    }

    public UserDTO authorize(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.findByEmailAndHash(email, hash)
                .orElseThrow(() -> new BadCredentialsException("incorrect login or password"));
        clientId = userModel.getId();
        return userDtoConverter.convert(userModel);
    }

    public UserDTO register(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.insert(email, hash);
        return userDtoConverter.convert(userModel);
    }

    public int getClientId() {
        return clientId;
    }
}