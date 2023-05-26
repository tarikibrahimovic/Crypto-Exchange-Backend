package com.example.demo.services.user;

import com.example.demo.config.JwtService;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.data.requestResponse.auth.AuthenticationResponse;
import com.example.demo.data.requestResponse.auth.LoginResponse;
import com.example.demo.data.requestResponse.user.BalanceRequest;
import com.example.demo.data.requestResponse.user.BalanceResponse;
import com.example.demo.data.requestResponse.user.ChangePasswordRequest;
import com.example.demo.data.requestResponse.user.ChangeUsernameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    public BalanceResponse balanceAdd(BalanceRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(user.getBalance() == null){
            user.setBalance(request.getAmount());
        }
        else {
            user.setBalance(user.getBalance() + request.getAmount());
        }
        repository.save(user);
        return BalanceResponse.builder()
                .balance(user.getBalance())
                .message("Balance added")
                .build();
    }

    public AuthenticationResponse changeUsername(ChangeUsernameRequest request, String token) {
        System.out.println(jwtService.extractId(token));
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(repository.existsByUsername(request.getUsername())){
            return AuthenticationResponse.builder()
                    .error("Username already exists")
                    .build();
        }
        user.setUsername(request.getUsername());
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Username changed!")
                .token(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Password changed!")
                .build();
    }

    public AuthenticationResponse deleteAccount(ChangePasswordRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder()
                    .error("Wrong password")
                    .build();
        }
        repository.delete(user);
        return AuthenticationResponse.builder()
                .message("Account deleted!")
                .build();
    }

    public LoginResponse refresh(String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        System.out.println("refreshed");
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
}
