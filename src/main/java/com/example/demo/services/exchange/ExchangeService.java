package com.example.demo.services.exchange;

import com.example.demo.config.JwtService;
import com.example.demo.entity.Exchange;
import com.example.demo.entity.User;
import com.example.demo.repository.ExchangeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requestResponse.exchange.ExchangeRequest;
import com.example.demo.requestResponse.exchange.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ExchangeRepository repository;
    public ExchangeResponse buyCoin(ExchangeRequest request, String token) {
        var user = userRepository.findById(jwtService.extractId(token)).orElseThrow();
        for(var exchange : user.getExchanges()){
            if(exchange.getCoinId().equals(request.getCoinId())){
                exchange.setCoinAmount(exchange.getCoinAmount() + request.getCoinAmount());
                user.setBalance(user.getBalance() - request.getCoinAmount() * request.getCoinPrice());
                repository.save(exchange);
                return ExchangeResponse.builder()
                        .message(request.getCoinAmount() + " coins bought")
                        .build();
            }
        }

        if(user.getBalance() == null || user.getBalance() < request.getCoinAmount() * request.getCoinPrice()){
            return ExchangeResponse.builder()
                    .error("Not enough money")
                    .build();
        }
        user.setBalance(user.getBalance() - request.getCoinAmount() * request.getCoinPrice());
        var exchange = Exchange.builder()
                .coinId(request.getCoinId())
                .coinAmount(request.getCoinAmount())
                .build();
        user.addExchange(exchange);
        repository.save(exchange);

        return ExchangeResponse.builder()
                .message("Coin bought")
                .build();
    }

    public ExchangeResponse sellCoin(ExchangeRequest request, String token) {
        var user = userRepository.findById(jwtService.extractId(token)).orElseThrow();
        var exchange = repository.findByCoinId(request.getCoinId()).orElseThrow();
        user.setBalance(user.getBalance() + request.getCoinAmount() * request.getCoinPrice());
        if(exchange.getCoinAmount() > request.getCoinAmount()){
            exchange.setCoinAmount(exchange.getCoinAmount() - request.getCoinAmount());
            repository.save(exchange);
            return ExchangeResponse.builder()
                    .message(request.getCoinAmount() + " coins sold")
                    .build();
        }
        else{
            repository.delete(exchange);
            user.removeExchange(exchange);
            return ExchangeResponse.builder()
                    .message("Coin sold")
                    .build();
        }

    }
}
