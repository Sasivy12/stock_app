package com.example.stock_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class StockAppApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(StockAppApplication.class, args);
	}

}