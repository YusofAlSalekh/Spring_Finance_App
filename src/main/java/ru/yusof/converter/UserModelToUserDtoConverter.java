package ru.yusof.converter;

import org.springframework.stereotype.Service;
import ru.yusof.entity.UserModel;
import ru.yusof.service.UserDTO;

@Service
public class UserModelToUserDtoConverter<S, T> implements Converter<UserModel, UserDTO> {

    @Override
    public UserDTO convert(UserModel source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setEmail(source.getEmail());
        return userDTO;
    }
}