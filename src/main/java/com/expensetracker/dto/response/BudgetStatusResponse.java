package com.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetStatusResponse {

    private int month;
    private int year;
    private BigDecimal budgetAmount;
    private BigDecimal totalExpense;
    private BigDecimal remainingAmount;
    private boolean budgetExceeded;
    private BigDecimal percentageUsed;
    private String message;
}