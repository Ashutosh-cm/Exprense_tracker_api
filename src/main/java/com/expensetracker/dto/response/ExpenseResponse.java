package com.expensetracker.dto.response;

import com.expensetracker.enums.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private String id;
    private String title;
    private BigDecimal amount;
    private ExpenseCategory category;
    private String note;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}