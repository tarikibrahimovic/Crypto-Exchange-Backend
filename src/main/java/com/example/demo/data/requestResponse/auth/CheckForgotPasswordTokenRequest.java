package com.example.demo.data.requestResponse.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckForgotPasswordTokenRequest {
    private String token;
    private String email;
}
