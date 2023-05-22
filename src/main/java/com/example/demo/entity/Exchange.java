package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {
    @Id
    @SequenceGenerator(
            name = "exchange_sequence",
            sequenceName = "exchange_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exchange_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private Double coinAmount;

    @Column(nullable = false)
    private String coinId;

}
