package com.example.demo.config;

import com.example.demo.data.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "34753778214125442A472D4B6150645367566B59703373367638792F423F4528";

    public final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    public Long extractId(String token){
        return Long.parseLong(extractClaims(token, claims -> claims.get("id", String.class)));
    }

    public String generateToken(UserDetails userDetails){
        final String id = userRepository.findByUsername(userDetails.getUsername()).get().getId().toString();
        return generateToken(userDetails, id);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        final String id = extractId(token).toString();
        final String userName = userRepository.findByEmail(userDetails.getUsername()).get().getUsername();
        final String idUser = userRepository.findByEmail(userDetails.getUsername()).get().getId().toString();
        return (username.equals(userName)) && !isTokenExpired(token) && id.equals(idUser);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails, String id){

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
