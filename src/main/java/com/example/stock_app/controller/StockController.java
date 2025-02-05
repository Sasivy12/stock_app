package com.example.stock_app.controller;

import com.example.stock_app.dto.BuyStockRequest;
import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.User;
import com.example.stock_app.model.UserDetails;
import com.example.stock_app.repository.UserRepository;
import com.example.stock_app.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<StockDTO> buyStock(@PathVariable String symbol,
                                             @PathVariable int quantity,
                                             @AuthenticationPrincipal UserDetails userDetails)
    {
        System.out.println("Extracted username: " + userDetails.getUsername());

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BuyStockRequest request = new BuyStockRequest(symbol, user.getId(), quantity);

        return ResponseEntity.ok(stockService.buyStock(request));
    }
}
