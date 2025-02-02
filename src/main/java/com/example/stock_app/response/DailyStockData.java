package com.example.stock_app.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DailyStockData
{
    @JsonProperty("4. close")
    private double closePrice;

    public DailyStockData()
    {

    }

    @JsonCreator
    public DailyStockData(@JsonProperty("4. close") double closePrice)
    {
        this.closePrice = closePrice;
    }
}
