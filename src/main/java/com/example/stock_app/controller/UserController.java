package com.example.stock_app.controller;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class UserController
{
    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello!";
    }
}
