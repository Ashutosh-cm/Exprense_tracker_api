package com.expensetracker.dto.request;

import com.expensetracker.enums.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Category is required")
    private ExpenseCategory category;

    private String note;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
}