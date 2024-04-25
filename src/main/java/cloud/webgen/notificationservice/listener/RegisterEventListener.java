package cloud.webgen.notificationservice.listener;

import cloud.webgen.notificationservice.events.RegisterEvent;
import cloud.webgen.notificationservice.service.EmailService;
import cloud.webgen.notificationservice.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${register-topic}")
    public void handleRegisterNotification(String message){
        var registerEvent = JsonUtils.fromJson(message, RegisterEvent.class);
        log.info("Recieved event {}", registerEvent);

        emailService.sendEmail(registerEvent.subject(), registerEvent.mail(), registerEvent.template());

    }
}
