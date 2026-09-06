package com.usmanaslam.authfortress.dto;
import java.util.List;
public record AuthResponse(String accessToken, String refreshToken, String tokenType, long expiresIn, String username, List<String> roles) {}
