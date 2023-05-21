package com.example.demo.controllers;

import com.example.demo.requestResponse.auth.AuthenticationResponse;
import com.example.demo.requestResponse.favorites.AddFavoriteRequest;
import com.example.demo.services.auth.AuthenticationService;
import com.example.demo.services.favorites.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService service;

    @PostMapping("/add")
    public ResponseEntity<AuthenticationResponse> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AddFavoriteRequest request
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.addFavorite(request, token));
    }

    @DeleteMapping("/remove/{coinId}")
    public ResponseEntity<AuthenticationResponse> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String coinId
    ){
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.removeFavorite(coinId, token));
    }
}
