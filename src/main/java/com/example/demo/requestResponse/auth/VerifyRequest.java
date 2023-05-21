package com.example.demo.requestResponse.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyRequest {
    private String token;
}
