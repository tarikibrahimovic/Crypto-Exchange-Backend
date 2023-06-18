package com.example.demo.controllers;

import com.example.demo.data.requestResponse.auth.AuthenticationResponse;
import com.example.demo.data.requestResponse.auth.LoginResponse;
import com.example.demo.data.requestResponse.user.*;
import com.example.demo.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.sql.Blob;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/balanceAdd")
    public ResponseEntity<BalanceResponse> balanceAdd(
            @RequestBody BalanceRequest request,
            @RequestHeader("Authorization") String authHeader
            ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.balanceAdd(request, token));
    }

    @PatchMapping("/changeUsername")
    public ResponseEntity<AuthenticationResponse> changeUsername(
            @RequestBody ChangeUsernameRequest request,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.changeUsername(request, token));
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<AuthenticationResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.changePassword(request, token));
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<AuthenticationResponse> deleteAccount(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.deleteAccount(request, token));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BalanceResponse> withdraw(
            @RequestBody BalanceRequest request,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.withdraw(request, token));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.refresh(token));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestHeader("Authorization") String authHeader
    ){
        System.out.println("Doslo je do kontrolera");
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.uploadImage(image, token));
    }


    @DeleteMapping("/deleteImage")
    public ResponseEntity<ImageResponse> deleteImage(
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.deleteImage(token));
    }
}
