package com.expensetracker.service.impl;

import com.expensetracker.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendBudgetAlert(String toEmail,
                                String userName,
                                int month,
                                int year,
                                BigDecimal budgetAmount,
                                BigDecimal totalExpense) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(toEmail);
            message.setSubject("Budget Alert - Smart Expense Tracker");

            message.setText(
                    "Hello " + userName + ",\n\n" +
                            "Your monthly budget has been crossed.\n\n" +
                            "Month: " + month + "\n" +
                            "Year: " + year + "\n" +
                            "Budget Amount: ₹" + budgetAmount + "\n" +
                            "Total Expense: ₹" + totalExpense + "\n\n" +
                            "Please review your expenses.\n\n" +
                            "Regards,\n" +
                            "Smart Expense Tracker"
            );

            javaMailSender.send(message);

        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }
}