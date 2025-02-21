package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.*;
import com.example.stock_app.repository.*;
import com.example.stock_app.response.DailyStockData;
import com.example.stock_app.response.StockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServiceTest
{
    @InjectMocks
    private PortfolioService portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StockApiClient stockApiClient;

    @Mock
    private UserActivityProducer userActivityProducer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserPortfolio_Success()
    {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(portfolioRepository.findByUserId(userId)).thenReturn(List.of(new Portfolio()));

        // Act
        List<Portfolio> portfolioList = portfolioService.getUserPortfolio(userId);

        // Assert
        assertNotNull(portfolioList);
        assertFalse(portfolioList.isEmpty());
    }

    @Test
    void testGetUserPortfolio_UserNotFound()
    {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> portfolioService.getUserPortfolio(userId));
    }

    @Test
    void testSellStock_Success()
    {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setBalance(1000.0);
        String symbol = "AAPL";
        int quantity = 5;

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setQuantity(10);
        portfolio.setStock(new Stock(1L, symbol, "Apple Inc.", 150.0, null));

        StockResponse stockResponse = new StockResponse();
        stockResponse.setTimeSeriesDaily(Map.of("2024-02-16", new DailyStockData(200.0)));

        when(portfolioRepository.findByUserAndStockSymbol(user, symbol)).thenReturn(Optional.of(portfolio));
        when(stockApiClient.getStockPrice(any(), eq(symbol), any())).thenReturn(stockResponse);

        // Act
        portfolioService.sellStock(user, symbol, quantity);

        // Assert
        assertEquals(2000.0, user.getBalance());
        verify(userRepository).save(user);
        verify(portfolioRepository).save(portfolio);
    }

    @Test
    void testSellStock_NotEnoughStocks()
    {
        // Arrange
        User user = new User();
        String symbol = "AAPL";
        int quantity = 10;
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setQuantity(5);

        when(portfolioRepository.findByUserAndStockSymbol(user, symbol)).thenReturn(Optional.of(portfolio));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> portfolioService.sellStock(user, symbol, quantity));
    }
}
