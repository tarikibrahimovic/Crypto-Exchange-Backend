package com.example.demo.services.exchange;

import com.example.demo.config.JwtService;
import com.example.demo.data.entity.Exchange;
import com.example.demo.data.repository.ExchangeRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.data.requestResponse.exchange.ExchangeRequest;
import com.example.demo.data.requestResponse.exchange.ExchangeResponse;
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
                exchange.setCoinPrice((request.getCoinPrice() + exchange.getCoinPrice()) / 2);
                user.setBalance(user.getBalance() - request.getCoinAmount() * request.getCoinPrice());
                repository.save(exchange);
                return ExchangeResponse.builder()
                        .message(request.getCoinAmount() + " coins bought")
                        .balance(user.getBalance())
                        .exchanges(user.getExchanges())
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
                .coinPrice(request.getCoinPrice())
                .coinAmount(request.getCoinAmount())
                .build();
        user.addExchange(exchange);
        repository.save(exchange);

        return ExchangeResponse.builder()
                .message("Coin bought")
                .balance(user.getBalance())
                .exchanges(user.getExchanges())
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
                    .balance(user.getBalance())
                    .exchanges(user.getExchanges())
                    .build();
        }
        else{
            repository.delete(exchange);
            user.removeExchange(exchange);
            return ExchangeResponse.builder()
                    .message("Coin sold")
                    .balance(user.getBalance())
                    .exchanges(user.getExchanges())
                    .build();
        }

    }
}
