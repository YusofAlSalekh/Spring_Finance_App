package yusof.service;

import yusof.converter.UserModelToUserDtoConverter;
import yusof.dao.UserDao;
import yusof.dao.UserModel;
import yusof.exceptions.CustomException;

public class AuthorizationService {
    private final UserDao userDao;
    private final DigestService digestService;
    private final UserModelToUserDtoConverter userDtoConverter;
    private int clientId;

    public AuthorizationService(UserDao userDao, DigestService digestService, UserModelToUserDtoConverter userDtoConverter) {
        this.userDao = userDao;
        this.digestService = digestService;
        this.userDtoConverter = userDtoConverter;
    }

    public UserDTO authorize(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.findByEmailAndHash(email, hash)
                .orElseThrow(() -> new CustomException("incorrect login or password"));
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
