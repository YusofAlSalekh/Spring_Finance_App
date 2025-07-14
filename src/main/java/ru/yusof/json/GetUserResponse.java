package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.UserDTO;

@Data
@AllArgsConstructor
public class GetUserResponse {
    private UserDTO user;
}
