package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

    List<Expense> findByUserIdOrderByExpenseDateDesc(String userId);

    Optional<Expense> findByIdAndUserId(String id, String userId);

    List<Expense> findByUserIdAndExpenseDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );
}