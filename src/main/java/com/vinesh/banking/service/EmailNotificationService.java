package com.vinesh.banking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailNotificationService(JavaMailSender mailSender,
                                    @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void sendTransactionAlert(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("Email sent to {}: {}", toEmail, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendDepositAlert(String toEmail, Long accountId, BigDecimal amount, BigDecimal newBalance) {
        String body = String.format(
            "Hello,\n\nA deposit of $%s has been credited to account #%d.\nNew balance: $%s\n\nThank you for banking with us.",
            amount, accountId, newBalance
        );
        sendTransactionAlert(toEmail, "Banking Alert: Deposit Received", body);
    }

    public void sendWithdrawalAlert(String toEmail, Long accountId, BigDecimal amount, BigDecimal newBalance) {
        String body = String.format(
            "Hello,\n\nA withdrawal of $%s has been debited from account #%d.\nNew balance: $%s\n\nIf you did not authorize this, contact us immediately.",
            amount, accountId, newBalance
        );
        sendTransactionAlert(toEmail, "Banking Alert: Withdrawal Processed", body);
    }

    public void sendTransferAlert(String toEmail, Long fromId, Long toId, BigDecimal amount, BigDecimal newBalance) {
        String body = String.format(
            "Hello,\n\n$%s has been transferred from account #%d to account #%d.\nNew balance: $%s\n\nThank you for banking with us.",
            amount, fromId, toId, newBalance
        );
        sendTransactionAlert(toEmail, "Banking Alert: Transfer Completed", body);
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        String body = String.format(
            "Hello %s,\n\nYour account has been created successfully.\nYou can now log in and start banking.\n\nWelcome aboard!",
            username
        );
        sendTransactionAlert(toEmail, "Welcome to Banking REST API!", body);
    }
}
