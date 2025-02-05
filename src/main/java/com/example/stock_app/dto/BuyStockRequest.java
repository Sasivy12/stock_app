package com.example.stock_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyStockRequest
{
    private String symbol;
    private Long userId;
    private int quantity;
}
