package com.example.stock_app.client;

import com.example.stock_app.response.StockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stockApiClient", url = "https://www.alphavantage.co")
public interface StockApiClient
{
    @GetMapping("/query")
    StockResponse getStockPrice(@RequestParam("function") String function,
                                @RequestParam("symbol") String symbol,
                                @RequestParam("apikey") String apiKey);
}
