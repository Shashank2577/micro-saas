package com.crosscutting.starter.notifications;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notification_preferences", schema = "cc")
@IdClass(NotificationPreferenceId.class)
public class NotificationPreference {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(nullable = false)
    private String channel;

    @Id
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean enabled = true;
}
