package com.usmanaslam.authfortress.service;

import com.usmanaslam.authfortress.dto.RoleUpdateRequest;
import com.usmanaslam.authfortress.dto.UserDto;
import com.usmanaslam.authfortress.exception.UserNotFoundException;
import com.usmanaslam.authfortress.model.Role;
import com.usmanaslam.authfortress.model.User;
import com.usmanaslam.authfortress.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuditService auditService;

    public UserService(UserRepository userRepository, AuditService auditService) {
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public UserDto updateRoles(Long userId, RoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        user.setRoles(request.roles().stream().map(Role::valueOf).collect(Collectors.toSet()));
        userRepository.save(user);
        
        auditService.log("ROLE_CHANGE", user.getUsername(), "unknown", "Roles updated to: " + request.roles());
        
        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::name).collect(Collectors.toSet()),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}
