package com.example.stock_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex)
    {
        ErrorResponse errorResponse = new ErrorResponse("USER_NOT_FOUND", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStockNotFound(StockNotFoundException ex)
    {
        ErrorResponse errorResponse = new ErrorResponse("STOCK_NOT_FOUND", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex)
    {
        ErrorResponse errorResponse = new ErrorResponse("USER_ALREADY_EXISTS", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
