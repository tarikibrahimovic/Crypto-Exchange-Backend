package com.example.demo.data.requestResponse.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleRegisterRequest {
    private String pictureUrl;
    private String username;
    private String email;
    private String password;
}
