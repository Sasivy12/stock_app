package com.example.stock_app.exception;

public class ErrorResponse
{
    public String errorCode;
    public String message;

    public ErrorResponse(String errorCode, String message)
    {
        this.errorCode = errorCode;
        this.message = message;
    }
}
