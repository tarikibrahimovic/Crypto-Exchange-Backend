package com.example.demo.controllers;

import com.example.demo.data.requestResponse.auth.*;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/proba")
    public ResponseEntity<String> nesto(
            @RequestBody RegisterRequest request
    ){
        System.out.println("nesto");
        return ResponseEntity.ok("nesto");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerifyRequest request
    ){
        return ResponseEntity.ok(service.verify(request));
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<AuthenticationResponse> sendEmail(
            @RequestBody SendEmailRequest email
    ){
        return ResponseEntity.ok(service.sendEmail(email));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<AuthenticationResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ){
        return ResponseEntity.ok(service.forgotPassword(request));
    }

    @PostMapping("/checkForgotPasswordToken")
    public ResponseEntity<AuthenticationResponse> checkForgotPasswordToken(
            @RequestBody CheckForgotPasswordTokenRequest request
    ){
        return ResponseEntity.ok(service.checkForgotPasswordToken(request));
    }

}
