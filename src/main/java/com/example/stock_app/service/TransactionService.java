package com.example.stock_app.service;

import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.TransactionRepository;
import com.example.stock_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService
{
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserActivityProducer userActivityProducer;

    public void addFunds(Long userId, double amount)
    {
        User user = userRepository.findById(userId).
                orElseThrow(() -> ( new UserNotFoundException("User with this id:" + userId + "is not found")));

        if(amount <= 0)
        {
            throw new RuntimeException("Amount cannot be null or smaller");
        }

        user.setBalance(user.getBalance() + amount);

        userActivityProducer.sendActivity("User increased his balance: " + user.getEmail());
        userRepository.save(user);
    }
}
