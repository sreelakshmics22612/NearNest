package com.nearnest.service;

import com.nearnest.dto.LoginRequest;
import com.nearnest.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);
}