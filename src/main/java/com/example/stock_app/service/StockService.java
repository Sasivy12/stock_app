package com.example.stock_app.service;

import com.example.stock_app.client.StockApiClient;
import com.example.stock_app.exception.StockNotFoundException;
import com.example.stock_app.model.Stock;
import com.example.stock_app.repository.PortfolioRepository;
import com.example.stock_app.repository.StockRepository;
import com.example.stock_app.response.DailyStockData;
import com.example.stock_app.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StockService
{

    private final StockRepository stockRepository;
    private final String API_KEY = "EXVGDTN70BIXNV5N";

    private final StockApiClient stockApiClient;

    private final PortfolioRepository portfolioRepository;

    public Stock getStockPrice(String symbol) {
        StockResponse stockResponse = stockApiClient.getStockPrice("TIME_SERIES_DAILY", symbol, API_KEY);

        System.out.println("API Response: " + stockResponse);

        if (stockResponse == null || stockResponse.getTimeSeriesDaily() == null) {
            throw new RuntimeException("No stock data available for symbol: " + symbol);
        }

        String latestDate = stockResponse.getTimeSeriesDaily().keySet().iterator().next();
        DailyStockData dailyStockData = stockResponse.getTimeSeriesDaily().get(latestDate);

        Stock stock = stockRepository.findBySymbol(symbol)
                .orElse(new Stock(null, symbol, symbol, 0.0, LocalDateTime.now()));

        stock.setPrice(dailyStockData.getClosePrice());
        stock.setName(symbol);

        stockRepository.save(stock);

        return stock;
    }
}
