package com.example.stock_app.controller;

import com.example.stock_app.model.Portfolio;
import com.example.stock_app.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController
{
    private final PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public List<Portfolio> getUserPortfolio(@PathVariable Long userId)
    {
        return portfolioService.getUserPortfolio(userId);
    }
}
