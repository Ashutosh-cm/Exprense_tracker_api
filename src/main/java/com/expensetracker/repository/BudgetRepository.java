package com.expensetracker.repository;

import com.expensetracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    List<Budget> findByUserIdOrderByYearDescMonthDesc(String userId);
    Optional<Budget> findByIdAndUserId(String id, String userId);
    Optional<Budget> findByUserIdAndMonthAndYear(String userId, int month, int year);
}