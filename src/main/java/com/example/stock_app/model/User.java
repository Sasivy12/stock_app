package com.example.stock_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Email should not be null")
    private String email;

    @NotEmpty(message = "Username should not be null")
    private String username;

    @NotEmpty(message = "Password should not be null")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

}