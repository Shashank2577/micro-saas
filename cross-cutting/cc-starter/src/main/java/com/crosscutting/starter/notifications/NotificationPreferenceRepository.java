package com.crosscutting.starter.notifications;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, NotificationPreferenceId> {

    List<NotificationPreference> findByUserId(UUID userId);

    List<NotificationPreference> findByUserIdAndChannel(UUID userId, String channel);

    Optional<NotificationPreference> findByUserIdAndChannelAndCategory(UUID userId, String channel, String category);
}
