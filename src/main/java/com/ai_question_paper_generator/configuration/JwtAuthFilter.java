package com.ai_question_paper_generator.configuration;

import com.ai_question_paper_generator.model.User;
import com.ai_question_paper_generator.repository.UserRepository;
import com.ai_question_paper_generator.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private JwtService jwtService;
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract auth header from request
        String authHeader = request.getHeader("Authorization");

        // Check auth header is not null and not start with Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        // Extract token from auth header
        String token = authHeader.substring(7);

        // Check token is valid or not
        if(!jwtService.isTokenValid(token)){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract email from token
        String email = jwtService.extractEmail(token);

        // Check email is not null
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Find user by email
            User user = userRepository.findByEmail(email).orElse(null);

            // Check user is not null
            if(user!=null){
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
