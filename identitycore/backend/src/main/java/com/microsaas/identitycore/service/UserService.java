package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.User;
import com.microsaas.identitycore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsersByTenant(UUID tenantId) {
        return userRepository.findByTenantId(tenantId);
    }

    public Optional<User> getUserById(UUID id, UUID tenantId) {
        return userRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public User createUser(UUID tenantId, User user) {
        user.setId(UUID.randomUUID());
        user.setTenantId(tenantId);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, UUID tenantId, User updatedUser) {
        return userRepository.findByIdAndTenantId(id, tenantId).map(user -> {
            user.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : user.getEmail());
            user.setFullName(updatedUser.getFullName() != null ? updatedUser.getFullName() : user.getFullName());
            user.setDepartment(updatedUser.getDepartment() != null ? updatedUser.getDepartment() : user.getDepartment());
            user.setRole(updatedUser.getRole() != null ? updatedUser.getRole() : user.getRole());
            user.setUpdatedAt(OffsetDateTime.now());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
