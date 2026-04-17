package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.domain.entity.Notification;
import com.microsaas.notificationhub.domain.entity.NotificationDelivery;
import com.microsaas.notificationhub.domain.repository.NotificationDeliveryRepository;
import com.microsaas.notificationhub.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final NotificationDeliveryRepository deliveryRepository;
    private final NotificationRepository notificationRepository;

    @Value("${sendgrid.api.key:dummy}")
    private String sendGridApiKey;

    @Value("${twilio.account.sid:dummy}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token:dummy}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number:+1234567890}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void initTwilio() {
        if (!"dummy".equals(twilioAccountSid) && !"dummy".equals(twilioAuthToken)) {
            Twilio.init(twilioAccountSid, twilioAuthToken);
        }
    }

    @Async
    @Transactional
    public void deliverNotification(Notification notification) {
        notification.setStatus("DELIVERING");
        notificationRepository.save(notification);

        boolean success = false;
        String responseMsg = "";
        String errorMsg = null;

        try {
            log.info("Delivering notification {} via {}", notification.getId(), notification.getChannel());

            if ("EMAIL".equalsIgnoreCase(notification.getChannel())) {
                if ("dummy".equals(sendGridApiKey)) {
                    responseMsg = "Simulated EMAIL delivery due to missing API key";
                    success = true;
                } else {
                    Email from = new Email("noreply@notificationhub.com");
                    String subject = notification.getSubject();
                    Email to = new Email(notification.getUserId() + "@example.com"); // Assuming userId is an email prefix or we'd map it
                    Content content = new Content("text/plain", notification.getContent());
                    Mail mail = new Mail(from, subject, to, content);

                    SendGrid sg = new SendGrid(sendGridApiKey);
                    Request request = new Request();
                    request.setMethod(Method.POST);
                    request.setEndpoint("mail/send");
                    request.setBody(mail.build());
                    Response response = sg.api(request);

                    success = response.getStatusCode() >= 200 && response.getStatusCode() < 300;
                    responseMsg = "Status Code: " + response.getStatusCode();
                    if (!success) {
                         errorMsg = response.getBody();
                    }
                }
            } else if ("SMS".equalsIgnoreCase(notification.getChannel())) {
                if ("dummy".equals(twilioAccountSid)) {
                    responseMsg = "Simulated SMS delivery due to missing API credentials";
                    success = true;
                } else {
                    Message message = Message.creator(
                            new PhoneNumber(notification.getUserId()), // Assuming userId is phone number
                            new PhoneNumber(twilioPhoneNumber),
                            notification.getContent())
                        .create();
                    success = message.getStatus() != Message.Status.FAILED && message.getStatus() != Message.Status.UNDELIVERED;
                    responseMsg = "Twilio message SID: " + message.getSid();
                    if (!success) {
                        errorMsg = "Twilio status: " + message.getStatus();
                    }
                }
            } else {
                 responseMsg = "Simulated delivery for channel: " + notification.getChannel();
                 success = true;
            }

            if (success) {
                NotificationDelivery delivery = NotificationDelivery.builder()
                        .id(UUID.randomUUID())
                        .tenantId(notification.getTenantId())
                        .notification(notification)
                        .channel(notification.getChannel())
                        .status("DELIVERED")
                        .providerResponse(responseMsg)
                        .attemptCount(1)
                        .opened(false)
                        .clicked(false)
                        .deliveredAt(ZonedDateTime.now())
                        .build();

                deliveryRepository.save(delivery);

                notification.setStatus("DELIVERED");
                notificationRepository.save(notification);
            } else {
                 throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            log.error("Failed to deliver notification {}", notification.getId(), e);

            NotificationDelivery delivery = NotificationDelivery.builder()
                    .id(UUID.randomUUID())
                    .tenantId(notification.getTenantId())
                    .notification(notification)
                    .channel(notification.getChannel())
                    .status("FAILED")
                    .errorMessage(e.getMessage() != null ? e.getMessage() : e.toString())
                    .attemptCount(1)
                    .build();

            deliveryRepository.save(delivery);

            notification.setStatus("FAILED");
            notificationRepository.save(notification);
        }
    }
}
