package com.example.stock_app.repository;

import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>
{
    List<Portfolio> findByUserId(Long userId);

    Optional<Portfolio> findByUserIdAndStockSymbol(Long userId, String symbol);

    Optional<Portfolio> findByUserAndStockSymbol(User user, String symbol);
}
