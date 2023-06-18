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
        emailSender.sendEmail(request.getEmail(), "Hello and welcome to the Crypto Exchange", "Hello " + user.getUsername() + ",\n\n" +
                "Your code is: " + random + "\n\n" +
                "Best regards,\n" +
                "Crypto Exchange Team");
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

    public AuthenticationResponse sendEmail(SendEmailRequest email) {
        var user = repository.findByEmail(email.getEmail()).orElseThrow();
        System.out.println(email.getType());
        if(user == null){
            return AuthenticationResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        var random = String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
        if(email.getType() == "Verification"){
            if(user.getVerificationToken() == null){
                return AuthenticationResponse.builder()
                        .error("Email is already verified")
                        .build();
            }
            user.setVerificationToken(random);
            repository.save(user);
            emailSender.sendEmail(email.getEmail(), "Hello and welcome to the Crypto Exchange", "Hello " + user.getUsername() + ",\n\n" +
                    "Your code is: " + random + "\n\n" +
                    "Best regards,\n" +
                    "Crypto Exchange Team");
        }
        else {
            emailSender.sendEmail(email.getEmail(), "Hello and welcome to the Crypto Exchange", "Hello " + user.getUsername() + ",\n\n" +
                    "Your ForgotPassword Token is: " + random + "\n\n" +
                    "Best regards,\n" +
                    "Crypto Exchange Team");
            user.setForgotPasswordToken(random);
            repository.save(user);
        }
        return AuthenticationResponse.builder()
                .message("Check your email")
                .build();
    }

    public AuthenticationResponse forgotPassword(ForgotPasswordRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        if(user == null){
            return AuthenticationResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        if(user.getForgotPasswordToken() == null){
            return AuthenticationResponse.builder()
                    .error("ForgotPassword token is invalid")
                    .build();
        }
        if(!user.getForgotPasswordToken().equals(request.getToken())){
            return AuthenticationResponse.builder()
                    .error("ForgotPassword token is invalid")
                    .build();
        }
        user.setForgotPasswordToken(null);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Your password is changed")
                .build();
    }

    public AuthenticationResponse checkForgotPasswordToken(CheckForgotPasswordTokenRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        if(user == null){
            return AuthenticationResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        if(user.getForgotPasswordToken() == null){
            return AuthenticationResponse.builder()
                    .error("ForgotPassword token is invalid")
                    .build();
        }
        if(!user.getForgotPasswordToken().equals(request.getToken())){
            return AuthenticationResponse.builder()
                    .error("ForgotPassword token is invalid")
                    .build();
        }
        return AuthenticationResponse.builder()
                .message("ForgotPassword token is valid")
                .build();
    }

    public LoginResponse google(GoogleRegisterRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        System.out.println(request.getEmail());
        if(user == null){
            return LoginResponse.builder()
                    .error("User does not exist")
                    .build();
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return LoginResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        return LoginResponse.builder()
                .token(jwtService.generateToken(user))
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .pictureUrl(user.getPictureUrl())
                .favorites(user.getFavorites())
                .balance(user.getBalance())
                .exchanges(user.getExchanges())
                .build();
    }

    public LoginResponse registerGoogle(GoogleRegisterRequest request) {
        if(repository.existsByEmail(request.getEmail())){
            return LoginResponse.builder()
                    .error("Email is already taken")
                    .build();
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .pictureUrl(request.getPictureUrl())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        return LoginResponse.builder()
                .token(jwtService.generateToken(user))
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .pictureUrl(user.getPictureUrl())
                .favorites(user.getFavorites())
                .balance(0.0)
                .exchanges(user.getExchanges())
                .build();
    }
}
