package com.crosscutting.starter.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(EmailChannel.class);

    @Override
    public void send(Notification notification) {
        log.info("EMAIL notification to user {} in tenant {} — subject: '{}', body: '{}'",
                notification.getUserId(), notification.getTenantId(),
                notification.getTitle(), notification.getBody());
    }

    @Override
    public String channelName() {
        return "email";
    }
}
