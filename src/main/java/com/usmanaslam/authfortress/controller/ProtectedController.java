package com.usmanaslam.authfortress.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
@Tag(name = "Protected Resources")
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

    @GetMapping("/user")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Hello User! You have USER access.");
    }

    @GetMapping("/moderator")
    public ResponseEntity<String> moderatorAccess() {
        return ResponseEntity.ok("Hello Moderator!");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Hello Admin!");
    }
}
