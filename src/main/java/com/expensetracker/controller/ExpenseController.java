package com.expensetracker.controller;

import com.expensetracker.dto.request.ExpenseRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.ExpenseResponse;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses() {
        return ResponseEntity.ok(expenseService.getMyExpenses());
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String expenseId) {
        return ResponseEntity.ok(expenseService.getExpenseById(expenseId));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable String expenseId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseId, request));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable String expenseId) {
        return ResponseEntity.ok(expenseService.deleteExpense(expenseId));
    }
}