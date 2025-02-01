package com.example.stock_app.service;

import com.example.stock_app.model.User;
import com.example.stock_app.model.UserDetails;
import com.example.stock_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);

        if(user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetails(user);
    }
}