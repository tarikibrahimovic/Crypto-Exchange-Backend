package com.example.demo.data.requestResponse.admin;

import com.example.demo.data.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPictureUrl(),
                user.getRole().name(),
                user.getBalance(),
                user.getExchanges(),
                user.getFavorites()
        );
    }
}
