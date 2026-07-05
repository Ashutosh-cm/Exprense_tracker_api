package com.expensetracker.service;

import java.math.BigDecimal;

public interface EmailService {

    void sendBudgetAlert(
            String toEmail,
            String userName,
            int month,
            int year,
            BigDecimal budgetAmount,
            BigDecimal totalExpense
    );
}