package cloud.webgen.notificationservice.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailService {

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.smtp.host}")
    private String smtpHost;

    @Value("${email.smtp.port}")
    private int smtpPort;

    private Session session;
    private ExecutorService executorService;

    @PostConstruct
    private void initialize() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.auth", "true");

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });

        executorService = Executors.newCachedThreadPool();
    }

    public void sendEmail(String subject, String emailTo, String content) {
        executorService.execute(() -> {
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailUsername));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                message.setSubject(subject);
                message.setContent(content, "text/html;charset=UTF-8");

                Transport.send(message);
            } catch (MessagingException e) {
                log.error("Failed to send email", e);
            }
        });
    }
}
