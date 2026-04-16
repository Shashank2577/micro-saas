package com.crosscutting.starter.notifications;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEntityTest {

    @Test
    void defaultDataIsEmptyMap() {
        Notification n = new Notification();
        assertThat(n.getData()).isNotNull().isEmpty();
    }

    @Test
    void allFieldsAreSettable() {
        Notification n = new Notification();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        Instant now = Instant.now();

        n.setId(id);
        n.setUserId(userId);
        n.setTenantId(tenantId);
        n.setChannel("email");
        n.setTitle("Welcome");
        n.setBody("Hello there");
        n.setData(Map.of("key", "value"));
        n.setSentAt(now);
        n.setReadAt(now);

        assertThat(n.getId()).isEqualTo(id);
        assertThat(n.getUserId()).isEqualTo(userId);
        assertThat(n.getTenantId()).isEqualTo(tenantId);
        assertThat(n.getChannel()).isEqualTo("email");
        assertThat(n.getTitle()).isEqualTo("Welcome");
        assertThat(n.getBody()).isEqualTo("Hello there");
        assertThat(n.getData()).containsEntry("key", "value");
        assertThat(n.getSentAt()).isEqualTo(now);
        assertThat(n.getReadAt()).isEqualTo(now);
    }

    @Test
    void notificationPreferenceFields() {
        NotificationPreference pref = new NotificationPreference();
        UUID userId = UUID.randomUUID();

        pref.setUserId(userId);
        pref.setChannel("in_app");
        pref.setCategory("alerts");
        pref.setEnabled(false);

        assertThat(pref.getUserId()).isEqualTo(userId);
        assertThat(pref.getChannel()).isEqualTo("in_app");
        assertThat(pref.getCategory()).isEqualTo("alerts");
        assertThat(pref.isEnabled()).isFalse();
    }

    @Test
    void notificationPreferenceDefaultEnabled() {
        NotificationPreference pref = new NotificationPreference();
        assertThat(pref.isEnabled()).isTrue();
    }
}
