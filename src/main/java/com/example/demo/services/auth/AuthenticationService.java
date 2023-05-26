package com.example.demo.services.auth;

import com.example.demo.data.requestResponse.auth.*;
import com.example.demo.config.JwtService;
import com.example.demo.services.email.EmailSender;
import com.example.demo.data.entity.Role;
import com.example.demo.data.entity.User;
import com.example.demo.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;

    public AuthenticationResponse register(RegisterRequest request) {
        if(repository.existsByEmail(request.getEmail())){
            return AuthenticationResponse.builder()
                    .error("Email already exists")
                    .build();
        }
        if(repository.existsByUsername(request.getUsername())){
            return AuthenticationResponse.builder()
                    .error("Username already exists")
                    .build();
        }
        var random = String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
        var user = User.builder()
                .username(request.getUsername())
                .verificationToken(random)
                .email(request.getEmail())
                .pictureUrl("")
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        emailSender.sendEmail(request.getEmail(), "Hello and welcome to the Crypto Exchange", "Your code is: " + random);
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Check your email")
                .build();
    }

    public LoginResponse login(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        if(user == null){
            return LoginResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        if(user.getVerificationToken() != null){
            return LoginResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return LoginResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .balance(user.getBalance())
                .role(user.getRole().name())
                .pictureUrl(user.getPictureUrl())
                .favorites(user.getFavorites())
                .balance(user.getBalance())
                .exchanges(user.getExchanges())
                .build();
    }

    public AuthenticationResponse verify(VerifyRequest request) {
        var user = repository.findByVerificationToken(request.getToken()).orElseThrow();
        System.out.println("User: " + user);
        if(user == null){
            return AuthenticationResponse.builder()
                    .error("Verification token is invalid")
                    .build();
        }
        user.setVerificationToken(null);
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Your email is verified")
                .token(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse sendEmail(String email) {
        var user = repository.findByEmail(email).orElseThrow();
        if(user == null){
            return AuthenticationResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        var random = String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
        user.setVerificationToken(random);
        repository.save(user);
        emailSender.sendEmail(email, "Hello and welcome to the Crypto Exchange", "Your code is: " + random);
        return AuthenticationResponse.builder()
                .message("Check your email")
                .build();
    }
}
