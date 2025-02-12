package com.example.stock_app.controller;

import com.example.stock_app.model.Portfolio;
import com.example.stock_app.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
@Tag(name = "Portfolio management", description = "API used for getting info about user's portfolio (purchased stocks")
public class PortfolioController
{
    private final PortfolioService portfolioService;

    @Operation(summary = "Get user's portfolio", description = "Returns all of user's purchased stocks")
    @GetMapping("/{userId}")
    public List<Portfolio> getUserPortfolio(@PathVariable Long userId)
    {
        return portfolioService.getUserPortfolio(userId);
    }
}
