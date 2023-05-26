package com.example.demo.data.requestResponse.auth;

import com.example.demo.data.entity.Exchange;
import com.example.demo.data.entity.Favorites;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private Double balance;
    private List<Exchange> exchanges;
    private String pictureUrl;
    private String role;
    private List<Favorites> favorites;
    private String message;
    private String error;
}
