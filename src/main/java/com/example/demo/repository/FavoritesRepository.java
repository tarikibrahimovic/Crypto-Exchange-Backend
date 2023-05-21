package com.example.demo.repository;

import com.example.demo.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    public boolean existsByCoinId(String CoinId);

    Optional<Favorites> findByCoinId(String coinId);

}
