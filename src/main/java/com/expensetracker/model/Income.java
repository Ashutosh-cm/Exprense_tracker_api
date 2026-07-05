package com.expensetracker.model;

import com.expensetracker.enums.IncomeCategory;
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
@Document(collection = "income")
public class Income {

    @Id
    private String id;

    private String userId;

    private String title;

    private BigDecimal amount;

    private IncomeCategory category;

    private String note;

    private LocalDate incomeDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}