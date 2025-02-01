package com.example.stock_app.config;

import com.example.stock_app.model.UserDetails;
import com.example.stock_app.service.JwtService;
import com.example.stock_app.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{

    private final JwtService jwtService;

    private final ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        String userEmail = null;
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            userEmail = jwtService.extractUserEmail(token);

            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails = (UserDetails) applicationContext.getBean(UserDetailsService.class).loadUserByUsername(userEmail);

                if(jwtService.validateToken(token, userDetails))
                {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);

    }
}
