package com.example.demo.data.requestResponse.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSendEmailRequest {
    private String email;
    private String subject;
    private String message;
}
