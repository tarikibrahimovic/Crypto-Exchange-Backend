package com.example.demo.data.repository;

import com.example.demo.data.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    public boolean existsByCoinId(String CoinId);

    Optional<Favorites> findByCoinId(String coinId);

}
