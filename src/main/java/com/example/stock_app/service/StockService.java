package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.dto.StockDTO;
import com.example.stock_app.exception.StockNotFoundException;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.Stock;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.PortfolioRepository;
import com.example.stock_app.repository.StockRepository;
import com.example.stock_app.repository.UserRepository;
import com.example.stock_app.response.DailyStockData;
import com.example.stock_app.response.StockResponse;
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
    private final String API_KEY = "EXVGDTN70BIXNV5N";
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

    public Stock buyStock(String symbol, User user, int quantity)
    {
        StockResponse stockResponse = stockApiClient.getStockPrice("TIME_SERIES_DAILY", symbol, API_KEY);

        if (stockResponse == null || stockResponse.getTimeSeriesDaily() == null)
        {
            throw new StockNotFoundException("No stock data available for symbol: " + symbol);
        }

        if(quantity == 0)
        {
            throw new RuntimeException("Quantity cannot be null");
        }

        String latestDate = stockResponse.getTimeSeriesDaily().keySet().iterator().next();
        DailyStockData dailyStockData = stockResponse.getTimeSeriesDaily().get(latestDate);

        Stock stock = stockRepository.findBySymbol(symbol)
                .orElseGet(() -> stockRepository.save(new Stock(null, symbol, symbol, 0.0, LocalDateTime.now())));

        stock.setPrice(dailyStockData.getClosePrice());
        stock.setName(symbol);

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));

        Portfolio portfolio = new Portfolio();
        portfolio.setStock(stock);
        portfolio.setBuyPrice(stock.getPrice());
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUser(existingUser);
        portfolio.setQuantity(quantity);

        portfolioRepository.save(portfolio);

        return stock;
    }
}
