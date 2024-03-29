package com.example.demo.data.requestResponse.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyRequest {
    private String email;
    private String token;
}
