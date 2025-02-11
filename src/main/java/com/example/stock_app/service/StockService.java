package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.dto.BuyStockRequest;
import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.exception.StockNotFoundException;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.Stock;
import com.example.stock_app.model.Transaction;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.PortfolioRepository;
import com.example.stock_app.repository.StockRepository;
import com.example.stock_app.repository.TransactionRepository;
import com.example.stock_app.repository.UserRepository;
import com.example.stock_app.response.DailyStockData;
import com.example.stock_app.response.StockResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StockService
{
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    private final String API_KEY = "xxxxxx";

    private final StockApiClient stockApiClient;


    public StockDTO getStockPrice(String symbol)
    {
        StockResponse stockResponse = stockApiClient.getStockPrice("TIME_SERIES_DAILY", symbol, API_KEY);

        System.out.println("API Response: " + stockResponse);

        if (stockResponse == null || stockResponse.getTimeSeriesDaily() == null)
        {
            throw new StockNotFoundException("No stock data available for symbol: " + symbol);
        }

        String latestDate = stockResponse.getTimeSeriesDaily().keySet().iterator().next();
        DailyStockData dailyStockData = stockResponse.getTimeSeriesDaily().get(latestDate);

        Stock stock = stockRepository.findBySymbol(symbol)
                .orElse(new Stock(null, symbol, symbol, 0.0, LocalDateTime.now()));

        stock.setPrice(dailyStockData.getClosePrice());
        stock.setName(symbol);

        stockRepository.save(stock);

        return new StockDTO(stock.getSymbol(), stock.getName(), stock.getPrice());
    }

    @Transactional
    public StockDTO buyStock(BuyStockRequest request)
    {
        StockResponse stockResponse = stockApiClient.getStockPrice("TIME_SERIES_DAILY", request.getSymbol(), API_KEY);

        if (stockResponse == null || stockResponse.getTimeSeriesDaily() == null)
        {
            throw new StockNotFoundException("No stock data available for symbol: " + request.getSymbol());
        }

        if(request.getQuantity() <= 0)
        {
            throw new RuntimeException("Quantity cannot be null");
        }

        String latestDate = stockResponse.getTimeSeriesDaily().keySet().iterator().next();
        DailyStockData dailyStockData = stockResponse.getTimeSeriesDaily().get(latestDate);

        Stock stock = stockRepository.findBySymbol(request.getSymbol())
                .orElseGet(() -> stockRepository.save(new Stock(null, request.getSymbol(), request.getSymbol(), 0.0, LocalDateTime.now())));

        stock.setPrice(dailyStockData.getClosePrice());
        stock.setName(request.getSymbol());

        User existingUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        double totalPrice = stock.getPrice() * request.getQuantity();

        if (existingUser.getBalance() < totalPrice)
        {
            throw new RuntimeException
                    ("Insufficient funds. Required: " + totalPrice + ", Available: " + existingUser.getBalance());
        }

        existingUser.setBalance(existingUser.getBalance() - totalPrice);
        userRepository.save(existingUser);

        Transaction transaction = new Transaction();
        transaction.setUser(existingUser);
        transaction.setStock(stock);
        transaction.setQuantity(request.getQuantity());
        transaction.setTotalPrice(totalPrice);
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        Portfolio portfolio = new Portfolio();
        portfolio.setStock(stock);
        portfolio.setBuyPrice(stock.getPrice());
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUser(existingUser);
        portfolio.setQuantity(request.getQuantity());

        portfolioRepository.save(portfolio);

        return new StockDTO(stock.getSymbol(), stock.getName(), stock.getPrice());
    }
}
