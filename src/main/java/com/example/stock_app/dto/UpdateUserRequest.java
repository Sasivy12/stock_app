package com.example.stock_app.dto;

import lombok.Data;

@Data
public class UpdateUserRequest
{
    private String email;

    private String username;

    private String password;

    private double balance;
}
