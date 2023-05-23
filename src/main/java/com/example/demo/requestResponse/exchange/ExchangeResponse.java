package com.example.demo.requestResponse.exchange;

import com.example.demo.entity.Exchange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {
    private Double balance;
    private List<Exchange> exchanges;
    private String message;
    private String error;
}
