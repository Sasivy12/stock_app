package com.example.stock_app.controller;

import com.example.stock_app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Add funds", description = "Increases user's balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The transaction is successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required or Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addfunds/{userId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> addFunds(@PathVariable("userId") Long userId, @RequestBody Map<String, Double> request)
    {
        double amount = request.get("amount");
        transactionService.addFunds(userId, amount);
        return ResponseEntity.ok("The transaction is successful");
    }
}
