package com.example.stock_app.service;

import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.TransactionRepository;
import com.example.stock_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest
{
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActivityProducer userActivityProducer;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFunds_Success()
    {
        // Arrange
        Long userId = 1L;
        double amount = 500.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(1000.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        transactionService.addFunds(userId, amount);

        // Assert
        assertEquals(1500.0, user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    void testAddFunds_UserNotFound()
    {
        // Arrange
        Long userId = 1L;
        double amount = 500.0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> transactionService.addFunds(userId, amount));
    }

    @Test
    void testAddFunds_InvalidAmount()
    {
        // Arrange
        Long userId = 1L;
        double amount = -100.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(1000.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> transactionService.addFunds(userId, amount));
    }
}
