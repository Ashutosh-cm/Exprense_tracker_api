package com.expensetracker.service.impl;

import com.expensetracker.dto.request.LoginRequest;
import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.AuthResponse;
import com.expensetracker.enums.Role;
import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.security.JwtService;
import com.expensetracker.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new AuthResponse("Invalid email or password", null, null, null, null);
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return new AuthResponse("Invalid email or password", null, null, null, null);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                "Login successful",
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}