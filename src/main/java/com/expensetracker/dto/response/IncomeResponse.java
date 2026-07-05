package com.expensetracker.dto.response;

import com.expensetracker.enums.IncomeCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponse {

    private String id;
    private String title;
    private BigDecimal amount;
    private IncomeCategory category;
    private String note;
    private LocalDate incomeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}