package com.github.flexca.sbae.backend.email.endpoint;

import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Slf4j
public class MailClient {

    @Value("${mail.username:''}")
    private String username;
    @Value("${mail.password:''}")
    private String password;

    @Value("${mail.smtp.host:'localhost'}")
    private String mailHost;
    @Value("${mail.smtp.port:1025}")
    private Integer mailPort;
    @Value("${mail.smtp.auth:false}")
    private Boolean mailSmtpAuth;
    @Value("${mail.smtp.starttls:false}")
    private Boolean mailSmtpStartTlsEnable;

    private final Properties properties = new Properties();

    @PostConstruct
    public void init() {
        properties.put("mail.smtp.auth", mailSmtpAuth);
        properties.put("mail.smtp.starttls.enable", mailSmtpStartTlsEnable);
        properties.put("mail.smtp.host", mailHost);
        properties.put("mail.smtp.port", mailPort);
    }

    public void sendEmail(String receiverEmail, String subject, String body) {

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch(Exception e) {
            log.error("Failed send email. Reason: {}", e.getMessage(), e);
        }
    }
}
