package com.example.demo.controllers;

import com.example.demo.requestResponse.exchange.ExchangeRequest;
import com.example.demo.requestResponse.exchange.ExchangeResponse;
import com.example.demo.services.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService service;

    @PostMapping("/buy")
    public ResponseEntity<ExchangeResponse> buyCoin(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ExchangeRequest request
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.buyCoin(request, token));
    }

    @PostMapping("/sell")
    public ResponseEntity<ExchangeResponse> sellCoin(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ExchangeRequest request
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.sellCoin(request, token));
    }
}
