package ru.yusof.service;

import org.springframework.stereotype.Service;
import ru.yusof.converter.Converter;
import ru.yusof.dao.UserDao;
import ru.yusof.entity.UserModel;
import ru.yusof.exceptions.BadCredentialsException;
import ru.yusof.exceptions.NotFoundException;

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

    public UserDTO getUserById(Integer userId) {
        UserModel user = userDao.findById(userId).orElseThrow(() -> new NotFoundException("User with such id not found"));
        if (user == null) {
            return null;
        }
        return userDtoConverter.convert(user);
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
        UserModel userModel = userDao.addUser(email, hash);
        return userDtoConverter.convert(userModel);
    }

    public int getClientId() {
        return clientId;
    }
}