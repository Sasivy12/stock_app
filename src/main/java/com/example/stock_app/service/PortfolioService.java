package com.example.stock_app.service;

import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.PortfolioRepository;
import com.example.stock_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService
{
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public List<Portfolio> getUserPortfolio(Long userId)
    {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id " + userId + " does not exist"));
        return  portfolioRepository.findByUserId(userId);
    }
}
