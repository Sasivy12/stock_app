package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.dto.BuyStockRequest;
import com.example.stock_app.exception.StockNotFoundException;
import com.example.stock_app.model.*;
import com.example.stock_app.repository.*;
import com.example.stock_app.response.DailyStockData;
import com.example.stock_app.response.StockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {
    @InjectMocks
    private StockService stockService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private StockApiClient stockApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStockPrice_Success() {
        // Arrange
        String symbol = "AAPL";
        StockResponse stockResponse = new StockResponse();
        stockResponse.setTimeSeriesDaily(Map.of("2024-02-16", new DailyStockData(150.0)));
        when(stockApiClient.getStockPrice(any(), eq(symbol), any())).thenReturn(stockResponse);
        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        StockDTO stockDTO = stockService.getStockPrice(symbol);

        // Assert
        assertNotNull(stockDTO);
        assertEquals(symbol, stockDTO.getSymbol());
        assertEquals(150.0, stockDTO.getPrice());
    }

    @Test
    void testGetStockPrice_StockNotFound() {
        // Arrange
        String symbol = "AAPL";
        when(stockApiClient.getStockPrice(any(), eq(symbol), any())).thenReturn(null);

        // Act & Assert
        assertThrows(StockNotFoundException.class, () -> stockService.getStockPrice(symbol));
    }

    @Test
    void testBuyStock_Success() {
        // Arrange
        BuyStockRequest request = new BuyStockRequest("AAPL", 1L, 10);
        StockResponse stockResponse = new StockResponse();
        stockResponse.setTimeSeriesDaily(Map.of("2024-02-16", new DailyStockData(150.0)));
        Stock stock = new Stock(1L, "AAPL", "Apple Inc.", 150.0, LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        user.setBalance(2000.0);

        when(stockApiClient.getStockPrice(any(), eq(request.getSymbol()), any())).thenReturn(stockResponse);
        when(stockRepository.findBySymbol(request.getSymbol())).thenReturn(Optional.of(stock));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));

        // Act
        StockDTO stockDTO = stockService.buyStock(request);

        // Assert
        assertNotNull(stockDTO);
        assertEquals("AAPL", stockDTO.getSymbol());
        assertEquals(150.0, stockDTO.getPrice());
        verify(userRepository).save(user);
        verify(transactionRepository).save(any(Transaction.class));
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void testBuyStock_InsufficientFunds() {
        // Arrange
        BuyStockRequest request = new BuyStockRequest("AAPL", 1L, 10);
        StockResponse stockResponse = new StockResponse();
        stockResponse.setTimeSeriesDaily(Map.of("2024-02-16", new DailyStockData(150.0)));
        Stock stock = new Stock(1L, "AAPL", "Apple Inc.", 150.0, LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        user.setBalance(500.0);

        when(stockApiClient.getStockPrice(any(), eq(request.getSymbol()), any())).thenReturn(stockResponse);
        when(stockRepository.findBySymbol(request.getSymbol())).thenReturn(Optional.of(stock));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> stockService.buyStock(request));
    }
}
