package com.ecommerce.backend.service;

import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(
            @Nullable String userEmail,
            String otp,
            String subject,
            String text) throws MessagingException {

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(userEmail);
            helper.setSubject(subject);

            // Send complete message
            helper.setText(text, false);

            javaMailSender.send(mimeMessage);

            log.info("Email sent successfully to {}", userEmail);

        } catch (MailException e) {

            log.error("Failed to send email to {}", userEmail, e);

            throw new MailSendException("Failed to send mail", e);
        }
    }
}