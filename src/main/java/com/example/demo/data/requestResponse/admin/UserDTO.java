package com.example.demo.data.requestResponse.admin;

import com.example.demo.data.entity.Exchange;
import com.example.demo.data.entity.Favorites;
import com.example.demo.data.entity.Role;

import java.util.List;

public record UserDTO(
        String username,
        String email,
        String pictureUrl,
        String role,
        Double balance,
        List<Exchange> exchanges,
        List<Favorites> favorites
) {
}
