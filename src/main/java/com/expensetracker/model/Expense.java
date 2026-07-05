package com.expensetracker.model;

import com.expensetracker.enums.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;

    private String userId;

    private String title;

    private BigDecimal amount;

    private ExpenseCategory category;

    private String note;

    private LocalDate expenseDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}