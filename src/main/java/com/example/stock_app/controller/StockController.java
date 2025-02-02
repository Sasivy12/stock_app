package com.example.stock_app.controller;

import com.example.stock_app.model.Stock;
import com.example.stock_app.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class StockController
{
    private final StockService stockService;

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStock(@PathVariable String symbol)
    {
        Stock stock = stockService.getStockPrice(symbol);

        return ResponseEntity.ok(stock);
    }
}
