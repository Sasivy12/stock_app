package com.example.stock_app.controller;

import com.example.stock_app.model.User;
import com.example.stock_app.service.UserService;
import jakarta.mail.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody User user)
    {
        userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws AuthenticationFailedException
    {
        return userService.verify(user);
    }
}
