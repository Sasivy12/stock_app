package com.example.stock_app.service;

import com.example.stock_app.model.Portfolio;
import com.example.stock_app.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService
{
    private final PortfolioRepository portfolioRepository;

    public List<Portfolio> getUserPortfolio(Long userId)
    {
        return  portfolioRepository.findByUserId(userId);
    }
}
