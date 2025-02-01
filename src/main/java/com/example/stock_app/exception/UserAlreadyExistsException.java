package com.example.stock_app.exception;

public class UserAlreadyExistsException extends RuntimeException
{
    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
