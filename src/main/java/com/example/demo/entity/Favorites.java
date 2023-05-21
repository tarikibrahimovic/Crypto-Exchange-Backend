package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Favorites {
    @Id
    @SequenceGenerator(
            name = "favorites_sequence",
            sequenceName = "favorites_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favorites_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String coinId;

    @ManyToMany(
            mappedBy = "favorites"
    )
    @JsonIgnore
    private List<User> users;

}
