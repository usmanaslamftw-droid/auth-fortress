package com.usmanaslam.authfortress.service;

import com.usmanaslam.authfortress.dto.*;
import com.usmanaslam.authfortress.exception.UserAlreadyExistsException;
import com.usmanaslam.authfortress.model.Role;
import com.usmanaslam.authfortress.model.User;
import com.usmanaslam.authfortress.repository.UserRepository;
import com.usmanaslam.authfortress.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;

    @Value("${jwt.access-token-expiration}")
    private long jwtExpirationInMs;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditService = auditService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateAccessToken(authentication);
        String refreshJwt = tokenProvider.generateRefreshToken(authentication);
        
        auditService.log("LOGIN", request.username(), "unknown", "Successful login");
        
        return new AuthResponse(jwt, refreshJwt, "Bearer", jwtExpirationInMs, request.username(), tokenProvider.getRolesFromToken(jwt));
    }

    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(user);
        auditService.log("REGISTER", request.username(), "unknown", "User registered");

        return new MessageResponse("User registered successfully");
    }

    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();
        
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            
            Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, 
                user.getRoles().stream().map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name())).toList());
                
            String newAccessToken = tokenProvider.generateAccessToken(auth);
            auditService.log("TOKEN_REFRESH", username, "unknown", "Token refreshed");
            return new AuthResponse(newAccessToken, refreshToken, "Bearer", jwtExpirationInMs, username, tokenProvider.getRolesFromToken(newAccessToken));
        }
        
        throw new RuntimeException("Invalid refresh token");
    }
}
