package com.usmanaslam.authfortress.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanaslam.authfortress.dto.LoginRequest;
import com.usmanaslam.authfortress.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerNewUser() throws Exception {
        RegisterRequest request = new RegisterRequest("newuser", "new@test.com", "pass123");
        
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedAdminEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/protected/admin"))
                .andExpect(status().isUnauthorized());
    }
}
