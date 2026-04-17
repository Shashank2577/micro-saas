package com.crosscutting.starter.auth;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSyncServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query nativeQuery;

    private UserSyncService userSyncService;

    @BeforeEach
    void setUp() {
        userSyncService = new UserSyncService(userRepository, entityManager);
        // Default stub: auto-onboard check returns 1 (user already has membership)
        lenient().when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
        lenient().when(nativeQuery.setParameter(anyString(), any())).thenReturn(nativeQuery);
        lenient().when(nativeQuery.getSingleResult()).thenReturn(1L);
    }

    @Test
    void syncUser_createsNewUserFromJwt() {
        UUID userId = UUID.randomUUID();
        String email = "new@example.com";
        String name = "New User";
        String picture = "https://example.com/avatar.png";

        Jwt jwt = buildJwt(userId.toString(), email, name, picture);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userSyncService.syncUser(jwt);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getDisplayName()).isEqualTo(name);
        assertThat(result.getAvatarUrl()).isEqualTo(picture);
        assertThat(result.getStatus()).isEqualTo("active");
        assertThat(result.getLastLoginAt()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void syncUser_updatesLastLoginForExistingUser() {
        UUID userId = UUID.randomUUID();
        Instant originalLogin = Instant.now().minusSeconds(3600);

        User existing = new User();
        existing.setId(userId);
        existing.setEmail("existing@example.com");
        existing.setLastLoginAt(originalLogin);
        existing.setCreatedAt(originalLogin);
        existing.setUpdatedAt(originalLogin);

        Jwt jwt = buildJwt(userId.toString(), "existing@example.com", "Existing", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userSyncService.syncUser(jwt);

        assertThat(result.getLastLoginAt()).isAfter(originalLogin);
        assertThat(result.getUpdatedAt()).isAfter(originalLogin);
        // email should not be overwritten on update
        assertThat(result.getEmail()).isEqualTo("existing@example.com");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(userId);
    }

    @Test
    void syncUser_handlesNullClaimsGracefully() {
        UUID userId = UUID.randomUUID();
        // JWT with no email, name, or picture claims
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userSyncService.syncUser(jwt);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isNull();
        assertThat(result.getDisplayName()).isNull();
        assertThat(result.getAvatarUrl()).isNull();
        assertThat(result.getStatus()).isEqualTo("active");
    }

    @Test
    void findById_delegatesToRepository() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userSyncService.findById(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
    }

    @Test
    void findById_returnsEmptyWhenNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userSyncService.findById(userId);

        assertThat(result).isEmpty();
    }

    private Jwt buildJwt(String subject, String email, String name, String picture) {
        var builder = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(subject)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600));

        if (email != null) {
            builder.claim("email", email);
        }
        if (name != null) {
            builder.claim("name", name);
        }
        if (picture != null) {
            builder.claim("picture", picture);
        }

        return builder.build();
    }
}
