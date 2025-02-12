package com.example.stock_app.service;

import com.example.stock_app.dto.UpdateUserRequest;
import com.example.stock_app.exception.UserAlreadyExistsException;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(User user)
    {
        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new UserAlreadyExistsException("User already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String verify(User user) throws AuthenticationFailedException {
        try
        {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            if (authentication.isAuthenticated())
            {
                return jwtService.generateToken(user.getEmail());
            }
        }
        catch (Exception e)
        {
            throw new AuthenticationFailedException("Authentication failed for user: " + user.getEmail());
        }
        return "FAILED";
    }

    public User getUserInfo(Long userId)
    {
        User existingUser = userRepository.findById(userId).orElseThrow(()
                -> (new UserNotFoundException("User with id " + userId + " does not exist")));

        return existingUser;
    }

    public String updateUser(String email, UpdateUserRequest updateUserRequest)
    {
        User existingUser = userRepository.findByEmail(updateUserRequest.getEmail()).orElseThrow(
                () -> (new UserNotFoundException("This user does not exist")));

        existingUser.setEmail(updateUserRequest.getEmail());
        existingUser.setPassword(updateUserRequest.getPassword());
        existingUser.setBalance(updateUserRequest.getBalance());
        existingUser.setUsername(updateUserRequest.getUsername());

        userRepository.save(existingUser);

        return "User successfully updated";
    }

}
