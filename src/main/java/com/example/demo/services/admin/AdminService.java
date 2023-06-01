package com.example.demo.services.admin;

import com.example.demo.config.JwtService;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.data.requestResponse.admin.AdminSendEmailRequest;
import com.example.demo.data.requestResponse.admin.UserDTO;
import com.example.demo.data.requestResponse.admin.UserDTOMapper;
import com.example.demo.data.requestResponse.auth.AuthenticationResponse;
import com.example.demo.services.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    public List<UserDTO> getUsers(String token) {
        var user = userRepository.findById(jwtService.extractId(token)).orElseThrow();
        if(!user.getRole().name().equals("ADMIN")){
            throw new RuntimeException("You are not an admin");
        }
        return userRepository.findAll().stream().map(userDTOMapper).collect(Collectors.toList());
    }

    public AuthenticationResponse deleteUser(String token, String email) {
        var user = userRepository.findById(jwtService.extractId(token)).orElseThrow();
        if(!user.getRole().name().equals("ADMIN")){
            throw new RuntimeException("You are not an admin");
        }
        var userToDelete = userRepository.findByEmail(email).orElseThrow();
        userRepository.delete(userToDelete);
        return AuthenticationResponse.builder().message("User deleted").build();
    }

    public AuthenticationResponse sendEmail(String token, AdminSendEmailRequest request) {
        var user = userRepository.findById(jwtService.extractId(token)).orElseThrow();
        if(!user.getRole().name().equals("ADMIN")){
            throw new RuntimeException("You are not an admin");
        }
        emailSender.sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
        return AuthenticationResponse.builder().message("Email sent").build();
    }
}
