package com.example.demo.data.requestResponse.exchange;

import com.example.demo.data.entity.Exchange;
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
