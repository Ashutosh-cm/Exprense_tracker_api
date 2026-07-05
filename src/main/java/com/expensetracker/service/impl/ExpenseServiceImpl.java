package com.expensetracker.service.impl;

import com.expensetracker.dto.request.ExpenseRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.ExpenseResponse;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.model.Budget;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.security.UserPrincipal;
import com.expensetracker.service.EmailService;
import com.expensetracker.service.ExpenseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final EmailService emailService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              BudgetRepository budgetRepository,
                              EmailService emailService) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
        this.emailService = emailService;
    }

    @Override
    public ExpenseResponse addExpense(ExpenseRequest request) {

        UserPrincipal currentUser = getCurrentUser();

        Expense expense = new Expense();
        expense.setUserId(currentUser.getId());
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setNote(request.getNote());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());

        Expense savedExpense = expenseRepository.save(expense);

        checkBudgetAndSendAlert(savedExpense, currentUser);

        return mapToResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getMyExpenses() {

        String userId = getCurrentUser().getId();

        return expenseRepository.findByUserIdOrderByExpenseDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExpenseResponse getExpenseById(String expenseId) {

        String userId = getCurrentUser().getId();

        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(String expenseId, ExpenseRequest request) {

        String userId = getCurrentUser().getId();

        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setNote(request.getNote());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setUpdatedAt(LocalDateTime.now());

        Expense updatedExpense = expenseRepository.save(expense);

        return mapToResponse(updatedExpense);
    }

    @Override
    public ApiResponse deleteExpense(String expenseId) {

        String userId = getCurrentUser().getId();

        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        expenseRepository.delete(expense);

        return new ApiResponse(true, "Expense deleted successfully");
    }

    private void checkBudgetAndSendAlert(Expense expense, UserPrincipal currentUser) {

        int month = expense.getExpenseDate().getMonthValue();
        int year = expense.getExpenseDate().getYear();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYear(currentUser.getId(), month, year)
                .orElse(null);

        if (budget == null) {
            return;
        }

        if (budget.isAlertSent()) {
            return;
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> monthlyExpenses = expenseRepository
                .findByUserIdAndExpenseDateBetween(currentUser.getId(), startDate, endDate);

        BigDecimal totalExpense = monthlyExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean budgetExceeded = totalExpense.compareTo(budget.getAmount()) > 0;

        if (budgetExceeded) {
            emailService.sendBudgetAlert(
                    currentUser.getEmail(),
                    currentUser.getName(),
                    month,
                    year,
                    budget.getAmount(),
                    totalExpense
            );

            budget.setAlertSent(true);
            budget.setUpdatedAt(LocalDateTime.now());
            budgetRepository.save(budget);
        }
    }

    private UserPrincipal getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserPrincipal) authentication.getPrincipal();
    }

    private ExpenseResponse mapToResponse(Expense expense) {

        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getNote(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }
}