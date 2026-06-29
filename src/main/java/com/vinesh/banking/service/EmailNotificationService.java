package com.vinesh.banking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// TODO: consider making this async with @Async so transactions don't block waiting for SMTP
@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTransactionAlert(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {}: {}", toEmail, subject);
        } catch (Exception e) {
            // don't let email failures break the transaction
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendDepositAlert(String toEmail, Long accountId, Double amount, Double newBalance) {
        String subject = "Banking Alert: Deposit Received";
        String body = String.format(
                "Hello,\n\nA deposit of $%.2f has been credited to account #%d.\nNew balance: $%.2f\n\nThank you for banking with us.",
                amount, accountId, newBalance
        );
        sendTransactionAlert(toEmail, subject, body);
    }

    public void sendWithdrawalAlert(String toEmail, Long accountId, Double amount, Double newBalance) {
        String subject = "Banking Alert: Withdrawal Processed";
        String body = String.format(
                "Hello,\n\nA withdrawal of $%.2f has been debited from account #%d.\nNew balance: $%.2f\n\nIf you did not authorize this, contact us immediately.",
                amount, accountId, newBalance
        );
        sendTransactionAlert(toEmail, subject, body);
    }

    public void sendTransferAlert(String toEmail, Long fromAccountId, Long toAccountId, Double amount, Double newBalance) {
        String subject = "Banking Alert: Transfer Completed";
        String body = String.format(
                "Hello,\n\n$%.2f has been transferred from account #%d to account #%d.\nNew balance: $%.2f\n\nThank you for banking with us.",
                amount, fromAccountId, toAccountId, newBalance
        );
        sendTransactionAlert(toEmail, subject, body);
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        String subject = "Welcome to Banking REST API!";
        String body = String.format(
                "Hello %s,\n\nYour account has been created successfully.\nYou can now log in and start banking.\n\nWelcome aboard!",
                username
        );
        sendTransactionAlert(toEmail, subject, body);
    }
}
