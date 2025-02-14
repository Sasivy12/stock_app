package com.example.stock_app.controller;

import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.Portfolio;
import com.example.stock_app.model.User;
import com.example.stock_app.model.UserDetails;
import com.example.stock_app.repository.UserRepository;
import com.example.stock_app.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
@Tag(name = "Portfolio management", description = "API used for getting info about user's portfolio (purchased stocks")
public class PortfolioController
{
    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    @Operation(summary = "Get user's portfolio", description = "Returns all of user's purchased stocks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchased stocks found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required or Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    @SecurityRequirement(name = "bearerAuth")
    public List<Portfolio> getUserPortfolio(@PathVariable Long userId)
    {
        return portfolioService.getUserPortfolio(userId);
    }

    @PostMapping("/{symbol}/sell/{quantity}")
    public ResponseEntity<String> sellStock(
            @PathVariable("symbol") String symbol, @PathVariable("quantity") int quantity,@AuthenticationPrincipal UserDetails userDetails)
    {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()
                -> new UserNotFoundException("This user does not exist"));

        portfolioService.sellStock(user, symbol, quantity);

        return ResponseEntity.ok("Stock successfully sold");
    }

}
