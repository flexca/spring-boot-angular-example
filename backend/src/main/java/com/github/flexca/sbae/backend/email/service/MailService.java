package com.github.flexca.sbae.backend.email.service;

import com.github.flexca.sbae.backend.email.endpoint.MailClient;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.flexca.sbae.backend.email.model.OrganizationRegistrationEmailModel;
import com.github.flexca.sbae.backend.email.model.UserRegistrationEmailModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final MailClient mailClient;

    public void sendOrganizationRegistrationEmail(String email, String registrationLink) {
        MustacheFactory mf = new DefaultMustacheFactory();
        ClassPathResource resource = new ClassPathResource("/templates/register-organization.mustache");
        try(Reader templateReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            Mustache mustache = mf.compile(templateReader,"register-organization");
            StringWriter writer = new StringWriter();
            OrganizationRegistrationEmailModel model = new OrganizationRegistrationEmailModel();
            model.setLink(registrationLink);
            mustache.execute(writer, model);
            writer.flush();
            mailClient.sendEmail(email, "Complete organization registration", writer.toString());
        } catch(Exception e) {
            log.error("Failed to send email. Reason: {}", e.getMessage(), e);
        }
    }

    public void sendUserRegistrationEmail(String email, String registrationLink) {
        MustacheFactory mf = new DefaultMustacheFactory();
        ClassPathResource resource = new ClassPathResource("/templates/register-user.mustache");
        try(Reader templateReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            Mustache mustache = mf.compile(templateReader,"register-user");
            StringWriter writer = new StringWriter();
            UserRegistrationEmailModel model = new UserRegistrationEmailModel();
            model.setLink(registrationLink);
            mustache.execute(writer, model);
            writer.flush();
            mailClient.sendEmail(email, "Complete organization registration", writer.toString());
        } catch(Exception e) {
            log.error("Failed to send email. Reason: {}", e.getMessage(), e);
        }
    }

    public void sendResetPasswordEmail(String email, String resetPasswordLink) {
        MustacheFactory mf = new DefaultMustacheFactory();
        ClassPathResource resource = new ClassPathResource("/templates/reset-password.mustache");
        try(Reader templateReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            Mustache mustache = mf.compile(templateReader,"reset-password");
            StringWriter writer = new StringWriter();
            OrganizationRegistrationEmailModel model = new OrganizationRegistrationEmailModel();
            model.setLink(resetPasswordLink);
            mustache.execute(writer, model);
            writer.flush();
            mailClient.sendEmail(email, "Complete password reset", writer.toString());
        } catch(Exception e) {
            log.error("Failed to send email. Reason: {}", e.getMessage(), e);
        }
    }
}
