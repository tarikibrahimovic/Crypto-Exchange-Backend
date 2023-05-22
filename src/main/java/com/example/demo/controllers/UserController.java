package com.example.demo.controllers;

import com.example.demo.requestResponse.auth.AuthenticationResponse;
import com.example.demo.requestResponse.user.BalanceRequest;
import com.example.demo.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/balanceAdd")
    public ResponseEntity<AuthenticationResponse> balanceAdd(
            @RequestBody BalanceRequest request,
            @RequestHeader("Authorization") String authHeader
            ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.balanceAdd(request, token));
    }
}
