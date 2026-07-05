package com.expensetracker.service;

import com.expensetracker.dto.request.BudgetRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.BudgetResponse;
import com.expensetracker.dto.response.BudgetStatusResponse;

import java.util.List;

public interface BudgetService {

    BudgetResponse setBudget(BudgetRequest request);

    List<BudgetResponse> getMyBudgets();

    BudgetStatusResponse getBudgetStatus(int month, int year);

    BudgetResponse updateBudget(String budgetId, BudgetRequest request);

    ApiResponse deleteBudget(String budgetId);
}