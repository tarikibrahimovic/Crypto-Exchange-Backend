package com.example.demo.data.repository;

import com.example.demo.data.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    public Optional<Exchange> findByCoinId(String coinId);
}
