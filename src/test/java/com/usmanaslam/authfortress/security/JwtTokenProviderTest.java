package com.usmanaslam.authfortress.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "mySecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLongForHS512Algorithm1234567890");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationInMs", 900000L);
        ReflectionTestUtils.setField(tokenProvider, "refreshExpirationInMs", 604800000L);
    }

    @Test
    void generateAndValidateToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String token = tokenProvider.generateAccessToken(auth);
        
        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals("testuser", tokenProvider.getUsernameFromToken(token));
        assertTrue(tokenProvider.getRolesFromToken(token).contains("ROLE_USER"));
    }
}
