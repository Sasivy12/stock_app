package com.example.stock_app.service;

import com.example.stock_app.dto.UpdateUserRequest;
import com.example.stock_app.exception.UserAlreadyExistsException;
import com.example.stock_app.exception.UserNotFoundException;
import com.example.stock_app.model.User;
import com.example.stock_app.repository.UserRepository;
import jakarta.mail.AuthenticationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest
{
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserActivityProducer userActivityProducer;

    private BCryptPasswordEncoder encoder;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);
    }

    @Test
    void testRegister_UserAlreadyExists()
    {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));

        // Verify no save operation
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        String result = userService.register(user);

        // Assert
        assertEquals("Registration successful", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testVerify_Success() throws AuthenticationFailedException
    {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("mocked-jwt-token");

        // Act
        String token = userService.verify(user);

        // Assert
        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void testVerify_Failure()
    {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> userService.verify(user));
    }

    @Test
    void testGetUserInfo_Success()
    {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserInfo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUserInfo_UserNotFound()
    {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(1L));
    }

    @Test
    void testUpdateUser_Success()
    {
        // Arrange
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("updated@example.com");
        updateUserRequest.setPassword("newpassword");
        updateUserRequest.setBalance(500.0);
        updateUserRequest.setUsername("updatedUser");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userRepository.findByEmail(updateUserRequest.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        String result = userService.updateUser("test@example.com", updateUserRequest);

        // Assert
        assertEquals("User successfully updated", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound()
    {
        // Arrange
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(updateUserRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser("test@example.com", updateUserRequest));
    }

}
