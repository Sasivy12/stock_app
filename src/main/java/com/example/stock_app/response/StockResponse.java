package com.example.stock_app.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class StockResponse
{
    private Map<String, DailyStockData> timeSeriesDaily;

    public StockResponse()
    {

    }

    @JsonCreator
    public StockResponse(@JsonProperty("Time Series (Daily)") Map<String, DailyStockData> timeSeriesDaily) {
        this.timeSeriesDaily = timeSeriesDaily;
    }
}
