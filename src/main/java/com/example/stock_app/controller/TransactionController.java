package com.example.stock_app.controller;

import com.example.stock_app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class TransactionController
{
    private final TransactionService transactionService;

    @PostMapping("/addfunds")
    public ResponseEntity<String> addFunds(@RequestParam Long userId, @RequestParam double amount)
    {
        transactionService.addFunds(userId, amount);
        return ResponseEntity.ok("The transaction is successful");
    }
}
