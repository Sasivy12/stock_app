package com.example.stock_app.controller;

import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.model.Stock;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.UserRepository;
import com.example.stock_app.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class StockController
{
    private final StockService stockService;
    private final UserRepository userRepository;

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStock(@PathVariable String symbol)
    {
        StockDTO stock = stockService.getStockPrice(symbol);

        return ResponseEntity.ok(stock);
    }

    @PostMapping("/{symbol}/buy/{quantity}")
    public ResponseEntity<Stock> buyStock(@PathVariable String symbol, @PathVariable int quantity, @RequestBody User user)
    {
        Stock stock = stockService.buyStock(symbol, user, quantity);

        return ResponseEntity.ok(stock);
    }
}
