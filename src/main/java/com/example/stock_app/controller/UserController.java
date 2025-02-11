package com.example.stock_app.controller;

import com.example.stock_app.dto.UpdateUserRequest;
import com.example.stock_app.model.User;
import com.example.stock_app.model.UserDetails;
import com.example.stock_app.service.UserService;
import jakarta.mail.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> login(@RequestBody User user) throws AuthenticationFailedException
    {
        return ResponseEntity.ok(userService.verify(user));
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable Long userId)
    {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request,@AuthenticationPrincipal UserDetails userDetails)
    {
        String email = userDetails.getUsername();

        return ResponseEntity.ok(userService.updateUser(email, request));
    }
}
