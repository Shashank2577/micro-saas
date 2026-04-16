package com.crosscutting.starter.notifications;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @Mock
    private InAppChannel inAppChannel;

    @Mock
    private EmailChannel emailChannel;

    private final UUID userId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    private NotificationService createService() {
        when(inAppChannel.channelName()).thenReturn("in_app");
        when(emailChannel.channelName()).thenReturn("email");
        return new NotificationService(notificationRepository, preferenceRepository,
                List.of(inAppChannel, emailChannel));
    }

    private void stubSave() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void send_dispatchesToInAppChannel() {
        NotificationService service = createService();
        stubSave();
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "in_app", "general"))
                .thenReturn(Optional.empty());

        Notification result = service.send(userId, tenantId, "in_app", "Hello", "World", null);

        assertThat(result.getTitle()).isEqualTo("Hello");
        assertThat(result.getBody()).isEqualTo("World");
        assertThat(result.getChannel()).isEqualTo("in_app");
        verify(inAppChannel).send(any(Notification.class));
    }

    @Test
    void send_dispatchesToEmailChannel() {
        NotificationService service = createService();
        stubSave();
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "email", "general"))
                .thenReturn(Optional.empty());

        Notification result = service.send(userId, tenantId, "email", "Subject", "Body", null);

        assertThat(result.getChannel()).isEqualTo("email");
        verify(emailChannel).send(any(Notification.class));
    }

    @Test
    void send_throwsForUnknownChannel() {
        NotificationService service = createService();

        assertThatThrownBy(() -> service.send(userId, tenantId, "sms", "Hi", "Body", null))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "VALIDATION_ERROR");
    }

    @Test
    void send_throwsWhenCategoryDisabledByPreference() {
        NotificationService service = createService();
        NotificationPreference pref = new NotificationPreference();
        pref.setUserId(userId);
        pref.setChannel("in_app");
        pref.setCategory("general");
        pref.setEnabled(false);
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "in_app", "general"))
                .thenReturn(Optional.of(pref));

        assertThatThrownBy(() -> service.send(userId, tenantId, "in_app", "Hi", "Body", null))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "VALIDATION_ERROR");
    }

    @Test
    void send_allowsWhenPreferenceEnabled() {
        NotificationService service = createService();
        stubSave();
        NotificationPreference pref = new NotificationPreference();
        pref.setEnabled(true);
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "in_app", "general"))
                .thenReturn(Optional.of(pref));

        Notification result = service.send(userId, tenantId, "in_app", "Hi", "Body", null);

        assertThat(result).isNotNull();
        verify(inAppChannel).send(any(Notification.class));
    }

    @Test
    void send_allowsWhenNoPrefExistsForCategory() {
        NotificationService service = createService();
        stubSave();
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "in_app", "alerts"))
                .thenReturn(Optional.empty());

        Notification result = service.send(userId, tenantId, "in_app", "Alert", "Important", null, "alerts");

        assertThat(result).isNotNull();
        verify(inAppChannel).send(any(Notification.class));
    }

    @Test
    void markAsRead_setsReadAtTimestamp() {
        NotificationService service = createService();
        UUID notifId = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setId(notifId);
        notification.setTitle("Test");
        when(notificationRepository.findById(notifId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Notification result = service.markAsRead(notifId);

        assertThat(result.getReadAt()).isNotNull();
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_throwsWhenNotFound() {
        NotificationService service = createService();
        UUID notifId = UUID.randomUUID();
        when(notificationRepository.findById(notifId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markAsRead(notifId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_NOT_FOUND");
    }

    @Test
    void listForUser_returnsPaginatedResults() {
        NotificationService service = createService();
        Pageable pageable = PageRequest.of(0, 10);
        Notification n1 = new Notification();
        n1.setTitle("N1");
        Page<Notification> page = new PageImpl<>(List.of(n1), pageable, 1);
        when(notificationRepository.findByUserIdAndTenantId(userId, tenantId, pageable))
                .thenReturn(page);

        Page<Notification> result = service.listForUser(userId, tenantId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("N1");
    }

    @Test
    void getPreferences_returnsUserPreferences() {
        NotificationService service = createService();
        NotificationPreference pref = new NotificationPreference();
        pref.setChannel("in_app");
        pref.setEnabled(true);
        when(preferenceRepository.findByUserId(userId))
                .thenReturn(List.of(pref));

        List<NotificationPreference> result = service.getPreferences(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getChannel()).isEqualTo("in_app");
    }

    @Test
    void updatePreference_createsNewWhenNotExists() {
        NotificationService service = createService();
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "email", "default"))
                .thenReturn(Optional.empty());
        when(preferenceRepository.save(any(NotificationPreference.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        NotificationPreference result = service.updatePreference(userId, "email", "default", false);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getChannel()).isEqualTo("email");
        assertThat(result.isEnabled()).isFalse();
        verify(preferenceRepository).save(any(NotificationPreference.class));
    }

    @Test
    void updatePreference_updatesExistingPreference() {
        NotificationService service = createService();
        NotificationPreference existing = new NotificationPreference();
        existing.setUserId(userId);
        existing.setChannel("email");
        existing.setCategory("default");
        existing.setEnabled(true);
        when(preferenceRepository.findByUserIdAndChannelAndCategory(userId, "email", "default"))
                .thenReturn(Optional.of(existing));
        when(preferenceRepository.save(any(NotificationPreference.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        NotificationPreference result = service.updatePreference(userId, "email", "default", false);

        assertThat(result.isEnabled()).isFalse();
        verify(preferenceRepository).save(existing);
    }
}
