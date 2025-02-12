package com.example.stock_app.controller;

import com.example.stock_app.dto.UpdateUserRequest;
import com.example.stock_app.model.User;
import com.example.stock_app.model.UserDetails;
import com.example.stock_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "API used for managing user registration, user login, getting user info and for updating user info")
public class UserController
{

    private final UserService userService;

    @Operation(summary = "Register user", description = "Registers new user")
    @PostMapping("/register")
    public void register(@RequestBody User user)
    {
        userService.register(user);
    }

    @Operation(summary = "Login user", description = "Logs in existing user")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws AuthenticationFailedException
    {
        return ResponseEntity.ok(userService.verify(user));
    }

    @Operation(summary = "Get user info", description = "Returns information about user by providing correct userID")
    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable Long userId)
    {
        return userService.getUserInfo(userId);
    }

    @Operation(summary = "Update user info", description = "Updates information about user")
    @PostMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request,@AuthenticationPrincipal UserDetails userDetails)
    {
        String email = userDetails.getUsername();

        return ResponseEntity.ok(userService.updateUser(email, request));
    }
}
