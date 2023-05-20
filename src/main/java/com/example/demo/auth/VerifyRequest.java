package com.example.demo.auth;

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
