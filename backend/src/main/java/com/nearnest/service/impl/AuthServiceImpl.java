package com.nearnest.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nearnest.dto.LoginRequest;
import com.nearnest.dto.LoginResponse;
import com.nearnest.dto.RegisterRequest;
import com.nearnest.model.Role;
import com.nearnest.model.User;
import com.nearnest.repository.UserRepository;
import com.nearnest.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(
                Role.valueOf(request.getRole())
        );

        userRepository.save(user);

        return "Registration Successful";
    }

  @Override
public LoginResponse login(LoginRequest request) {

    User user = userRepository
            .findByEmail(request.getEmail())
            .orElse(null);

    if (user == null) {
        return null;
    }

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        return null;
    }

    return new LoginResponse(
            user.getId(),
            user.getRole().name()
    );
}
}