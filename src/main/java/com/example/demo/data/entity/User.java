package com.example.demo.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(nullable = true)
    private String pictureUrl;
    @Column(nullable = true)
    private Double balance = 0.0;
    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_favorites_map",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "favorites_id",
                    referencedColumnName = "id"
            )
    )
    private List<Favorites> favorites;

    public void addFavorites(Favorites favorite){
        if (favorites == null) favorites = new ArrayList<>();
        if(!favorites.contains(favorite))
            favorites.add(favorite);
    }
    public void removeFavorites(Favorites favorite) {
        favorites.remove(favorite);
    }

    @Column(nullable = true)
    private String verificationToken;

    @Column(nullable = true)
    private Date VerifiedAt;

    @Column(nullable = true)
    private String forgotPasswordToken;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private List<Exchange> exchanges;

    private String type;

    public void addExchange(Exchange exchange){
        if (exchanges == null) exchanges = new ArrayList<>();
            exchanges.add(exchange);
    }

    public void removeExchange(Exchange exchange){
        exchanges.remove(exchange);
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }
}
