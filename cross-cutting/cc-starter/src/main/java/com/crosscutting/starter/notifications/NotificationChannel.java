package com.crosscutting.starter.notifications;

public interface NotificationChannel {

    void send(Notification notification);

    String channelName();
}
