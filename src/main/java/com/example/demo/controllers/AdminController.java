package com.example.demo.controllers;

import com.example.demo.data.requestResponse.admin.AdminSendEmailRequest;
import com.example.demo.data.requestResponse.admin.DeleteUserRequest;
import com.example.demo.data.requestResponse.admin.UserDTO;
import com.example.demo.data.requestResponse.auth.AuthenticationResponse;
import com.example.demo.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.getUsers(token));
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<AuthenticationResponse> deleteUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody DeleteUserRequest request
            ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.deleteUser(token, request.getEmail()));
    }


    @PostMapping("/sendEmail")
    public ResponseEntity<AuthenticationResponse> sendEmail(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AdminSendEmailRequest request
            ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.sendEmail(token, request));
    }

}
