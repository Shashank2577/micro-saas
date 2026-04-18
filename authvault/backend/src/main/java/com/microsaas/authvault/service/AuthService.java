package com.microsaas.authvault.service;

import com.microsaas.authvault.dto.AuthResponse;
import com.microsaas.authvault.dto.LoginRequest;
import com.microsaas.authvault.dto.MfaVerifyRequest;
import com.microsaas.authvault.dto.UserDto;
import com.microsaas.authvault.entity.User;
import com.microsaas.authvault.repository.AuthVaultUserRepository;
import com.microsaas.authvault.security.JwtService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crosscutting.starter.tenancy.TenantContext;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthVaultUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecretGenerator secretGenerator;
    private final CodeVerifier codeVerifier;

    public AuthResponse login(LoginRequest request) {
        UUID tenantId = TenantContext.require();
        User user = userRepository.findByTenantIdAndEmail(tenantId, request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.isMfaEnabled()) {
            return AuthResponse.builder()
                    .requiresMfa(true)
                    .build();
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .requiresMfa(false)
                .build();
    }

    public AuthResponse verifyMfa(MfaVerifyRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!codeVerifier.isValidCode(user.getMfaSecret(), request.getCode())) {
            throw new RuntimeException("Invalid MFA code");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .requiresMfa(false)
                .build();
    }

    public User register(UserDto dto) {
        UUID tenantId = TenantContext.require();
        if (userRepository.findByTenantIdAndEmail(tenantId, dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .tenantId(tenantId)
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .mfaEnabled(false)
                .status("ACTIVE")
                .build();
        return userRepository.save(user);
    }

    public String setupMfa(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String secret = secretGenerator.generate();
        user.setMfaSecret(secret);
        userRepository.save(user);
        return secret;
    }
}
