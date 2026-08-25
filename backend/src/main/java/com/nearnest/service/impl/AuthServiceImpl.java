package com.nearnest.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nearnest.dto.RegisterRequest;
import com.nearnest.dto.LoginRequest;
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

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.valueOf(request.getRole()));

        userRepository.save(user);

        return "Registration Successful";
    }
    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            return "Invalid email or password";
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return "Invalid email or password";
        }

        return user.getRole().name();
    }
}