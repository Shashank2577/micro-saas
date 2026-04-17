package com.crosscutting.starter.notifications;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private final UUID userId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void list_returnsPagedNotifications() throws Exception {
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setUserId(userId);
        n.setTenantId(tenantId);
        n.setChannel("in_app");
        n.setTitle("Hello");
        n.setBody("World");
        n.setCreatedAt(Instant.now());

        when(notificationService.listForUser(eq(userId), eq(tenantId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(n), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/cc/notifications")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Hello"))
                .andExpect(jsonPath("$.content[0].body").value("World"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void markAsRead_returnsUpdatedNotification() throws Exception {
        UUID notifId = UUID.randomUUID();
        Notification n = new Notification();
        n.setId(notifId);
        n.setTitle("Test");
        n.setReadAt(Instant.now());
        when(notificationService.markAsRead(notifId)).thenReturn(n);

        mockMvc.perform(put("/cc/notifications/" + notifId + "/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.readAt").isNotEmpty());
    }

    @Test
    void send_createsAndReturnsNotification() throws Exception {
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setUserId(userId);
        n.setTenantId(tenantId);
        n.setChannel("in_app");
        n.setTitle("Alert");
        n.setBody("Something happened");

        when(notificationService.send(eq(userId), eq(tenantId), eq("in_app"), eq("Alert"), eq("Something happened"), any()))
                .thenReturn(n);

        mockMvc.perform(post("/cc/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":"%s","channel":"in_app","title":"Alert","body":"Something happened"}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Alert"))
                .andExpect(jsonPath("$.channel").value("in_app"));
    }

    @Test
    void getPreferences_returnsPreferenceList() throws Exception {
        NotificationPreference pref = new NotificationPreference();
        pref.setUserId(userId);
        pref.setChannel("in_app");
        pref.setCategory("default");
        pref.setEnabled(true);

        when(notificationService.getPreferences(userId))
                .thenReturn(List.of(pref));

        mockMvc.perform(get("/cc/notifications/preferences")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].channel").value("in_app"))
                .andExpect(jsonPath("$[0].enabled").value(true));
    }

    @Test
    void updatePreference_returnsUpdatedPreference() throws Exception {
        NotificationPreference pref = new NotificationPreference();
        pref.setUserId(userId);
        pref.setChannel("email");
        pref.setCategory("default");
        pref.setEnabled(false);

        when(notificationService.updatePreference(userId, "email", "default", false))
                .thenReturn(pref);

        mockMvc.perform(put("/cc/notifications/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":"%s","channel":"email","category":"default","enabled":false}
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel").value("email"))
                .andExpect(jsonPath("$.enabled").value(false));
    }
}
