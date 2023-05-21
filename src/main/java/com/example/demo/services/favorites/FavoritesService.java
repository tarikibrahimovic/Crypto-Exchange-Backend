package com.example.demo.services.favorites;

import com.example.demo.config.JwtService;
import com.example.demo.entity.Favorites;
import com.example.demo.repository.FavoritesRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requestResponse.auth.AuthenticationResponse;
import com.example.demo.requestResponse.favorites.AddFavoriteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository repository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public AuthenticationResponse addFavorite(AddFavoriteRequest request, String token) {
        var id = jwtService.extractId(token);
        var user = userRepository.findById(id).orElseThrow();
        if(!repository.existsByCoinId(request.getCoinId())){
            var favorite = Favorites.builder()
                    .coinId(request.getCoinId())
                    .build();
            user.addFavorites(favorite);
            repository.save(favorite);
        }
        else{
            var favorite = repository.findByCoinId(request.getCoinId()).orElseThrow();
            user.addFavorites(favorite);
            repository.save(favorite);
        }
        return AuthenticationResponse.builder()
                .message("Added to favorites")
                .build();
    }

    public AuthenticationResponse removeFavorite(String coinId, String token) {
        var id = jwtService.extractId(token);
        var user = userRepository.findById(id).orElseThrow();
        var favorite = repository.findByCoinId(coinId).orElseThrow();
        user.removeFavorites(favorite);
        repository.save(favorite);
        return AuthenticationResponse.builder()
                .message("Removed from favorites")
                .build();
    }
}
