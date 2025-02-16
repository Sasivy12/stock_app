package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.exception.StockNotFoundException;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.Stock;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.PortfolioRepository;
import com.example.stock_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService
{
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final StockApiClient stockApiClient;
    private final String API_KEY = "xxxxxx";


    public List<Portfolio> getUserPortfolio(Long userId)
    {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id " + userId + " does not exist"));
        return  portfolioRepository.findByUserId(userId);
    }

    public void sellStock(User user, String symbol, int quantity)
    {
        Portfolio portfolio = portfolioRepository.findByUserAndStockSymbol(user, symbol)
                .orElseThrow(() -> new RuntimeException("User does not own this stock"));

        if (portfolio.getQuantity() < quantity)
        {
            throw new RuntimeException("Not enough stocks to sell");
        }

        double sellPrice = stockApiClient.getStockPrice("TIME_SERIES_DAILY", symbol, API_KEY)
                .getTimeSeriesDaily().values().iterator().next().getClosePrice();

        double totalEarnings = sellPrice * quantity;
        user.setBalance(user.getBalance() + totalEarnings);

        if(portfolio.getQuantity() == quantity)
        {
            portfolioRepository.delete(portfolio);
        }
        else
        {
            portfolio.setQuantity(portfolio.getQuantity() - quantity);
            portfolioRepository.save(portfolio);
        }

        userRepository.save(user);
    }
}
