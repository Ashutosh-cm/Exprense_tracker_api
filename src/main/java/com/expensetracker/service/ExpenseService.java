package com.expensetracker.service;

import com.expensetracker.dto.request.ExpenseRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse addExpense(ExpenseRequest request);

    List<ExpenseResponse> getMyExpenses();

    ExpenseResponse getExpenseById(String expenseId);

    ExpenseResponse updateExpense(String expenseId, ExpenseRequest request);

    ApiResponse deleteExpense(String expenseId);
}