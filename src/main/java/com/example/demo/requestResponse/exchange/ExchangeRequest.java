package com.example.demo.requestResponse.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {
    private Double coinAmount;
    private Double coinPrice;
    private String coinId;
}
