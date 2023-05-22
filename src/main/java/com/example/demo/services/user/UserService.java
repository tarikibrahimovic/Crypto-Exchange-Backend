package com.example.demo.services.user;

import com.example.demo.config.JwtService;
import com.example.demo.repository.UserRepository;
import com.example.demo.requestResponse.auth.AuthenticationResponse;
import com.example.demo.requestResponse.user.BalanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtService jwtService;
    public AuthenticationResponse balanceAdd(BalanceRequest request, String token) {
        var user = repository.findById(jwtService.extractId(token)).orElseThrow();
        if(user.getBalance() == null){
            user.setBalance(request.getAmount());
        }
        else {
            user.setBalance(user.getBalance() + request.getAmount());
        }
        repository.save(user);
        return AuthenticationResponse.builder()
                .message("Balance added")
                .build();
    }
}
