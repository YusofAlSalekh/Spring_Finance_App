package yusof.service;

import yusof.converter.UserModelToUserDtoConverter;
import yusof.dao.UserDao;
import yusof.dao.UserModel;
import yusof.exception.CustomException;

public class AuthorizationService {
    private final UserDao userDao;
    private final DigestService digestService;
    private final UserModelToUserDtoConverter userDtoConverter;
    private int clientId;

    public AuthorizationService() {
        this.userDao = new UserDao();
        this.digestService = new Md5DigestService();
        this.userDtoConverter = new UserModelToUserDtoConverter();
    }

    public UserDTO authorize(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.findByEmailAndHash(email, hash);
        if (userModel != null) {
            clientId = userModel.getId();
            return userDtoConverter.convert(userModel);
        } else {
            throw new CustomException("incorrect login or password");
        }
    }

    public UserDTO register(String email, String password) {
        String hash = digestService.hex(password);
        UserModel userModel = userDao.insert(email, hash);
        if (userModel == null) {
            return null;
        } else {
            return userDtoConverter.convert(userModel);
        }
    }

    public int getClientId() {
        return clientId;
    }
}
