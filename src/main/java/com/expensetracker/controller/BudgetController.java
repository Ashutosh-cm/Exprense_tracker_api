package com.expensetracker.controller;

import com.expensetracker.dto.request.BudgetRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.BudgetResponse;
import com.expensetracker.dto.response.BudgetStatusResponse;
import com.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> setBudget(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.setBudget(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getMyBudgets() {
        return ResponseEntity.ok(budgetService.getMyBudgets());
    }

    @GetMapping("/status")
    public ResponseEntity<BudgetStatusResponse> getBudgetStatus(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(budgetService.getBudgetStatus(month, year));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable String budgetId,
            @Valid @RequestBody BudgetRequest request
    ) {
        return ResponseEntity.ok(budgetService.updateBudget(budgetId, request));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable String budgetId) {
        return ResponseEntity.ok(budgetService.deleteBudget(budgetId));
    }
}