package com.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "budgets")
public class Budget {

    @Id
    private String id;

    private String userId;

    private int month;

    private int year;

    private BigDecimal amount;

    private boolean alertSent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}