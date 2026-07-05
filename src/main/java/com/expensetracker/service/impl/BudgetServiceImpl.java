package com.expensetracker.service.impl;

import com.expensetracker.dto.request.BudgetRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.BudgetResponse;
import com.expensetracker.dto.response.BudgetStatusResponse;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.model.Budget;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.security.UserPrincipal;
import com.expensetracker.service.BudgetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public BudgetResponse setBudget(BudgetRequest request) {

        String userId = getCurrentUserId();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYear(userId, request.getMonth(), request.getYear())
                .orElse(new Budget());

        budget.setUserId(userId);
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setAmount(request.getAmount());
        budget.setAlertSent(false);

        if (budget.getCreatedAt() == null) {
            budget.setCreatedAt(LocalDateTime.now());
        }

        budget.setUpdatedAt(LocalDateTime.now());

        Budget savedBudget = budgetRepository.save(budget);

        return mapToResponse(savedBudget);
    }

    @Override
    public List<BudgetResponse> getMyBudgets() {

        String userId = getCurrentUserId();

        return budgetRepository.findByUserIdOrderByYearDescMonthDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BudgetStatusResponse getBudgetStatus(int month, int year) {

        String userId = getCurrentUserId();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found for this month and year"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> monthlyExpenses = expenseRepository
                .findByUserIdAndExpenseDateBetween(userId, startDate, endDate);

        BigDecimal totalExpense = monthlyExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingAmount = budget.getAmount().subtract(totalExpense);

        boolean budgetExceeded = totalExpense.compareTo(budget.getAmount()) > 0;

        BigDecimal percentageUsed = totalExpense
                .multiply(BigDecimal.valueOf(100))
                .divide(budget.getAmount(), 2, RoundingMode.HALF_UP);

        String message;

        if (budgetExceeded) {
            message = "Budget crossed! You have spent more than your monthly budget.";
        } else {
            message = "Budget is under control.";
        }

        return new BudgetStatusResponse(
                month,
                year,
                budget.getAmount(),
                totalExpense,
                remainingAmount,
                budgetExceeded,
                percentageUsed,
                message
        );
    }

    @Override
    public BudgetResponse updateBudget(String budgetId, BudgetRequest request) {

        String userId = getCurrentUserId();

        Budget budget = budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setAmount(request.getAmount());
        budget.setAlertSent(false);
        budget.setUpdatedAt(LocalDateTime.now());

        Budget updatedBudget = budgetRepository.save(budget);

        return mapToResponse(updatedBudget);
    }

    @Override
    public ApiResponse deleteBudget(String budgetId) {

        String userId = getCurrentUserId();

        Budget budget = budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        budgetRepository.delete(budget);

        return new ApiResponse(true, "Budget deleted successfully");
    }

    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userPrincipal.getId();
    }

    private BudgetResponse mapToResponse(Budget budget) {

        return new BudgetResponse(
                budget.getId(),
                budget.getMonth(),
                budget.getYear(),
                budget.getAmount(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }
}