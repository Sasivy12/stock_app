package com.example.stock_app.controller;

import com.example.stock_app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Tag(name = "Transaction Management", description = "API is user for adding funds to user's balance")
public class TransactionController
{
    private final TransactionService transactionService;

    @PostMapping("/addfunds/{userId}")
    @Operation(summary = "Add funds", description = "Increases user's balance")
    public ResponseEntity<String> addFunds(@PathVariable("userId") Long userId, @RequestBody Map<String, Double> request)
    {
        double amount = request.get("amount");
        transactionService.addFunds(userId, amount);
        return ResponseEntity.ok("The transaction is successful");
    }
}
