package com.usmanaslam.authfortress.dto;
import java.time.LocalDateTime;
import java.util.Set;
public record UserDto(Long id, String username, String email, Set<String> roles, boolean enabled, LocalDateTime createdAt) {}
