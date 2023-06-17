package com.example.demo.data.requestResponse.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendEmailRequest {
    private String email;
    private String type;
}
