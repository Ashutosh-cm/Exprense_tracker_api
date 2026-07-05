package com.expensetracker.controller;

import com.expensetracker.dto.request.IncomeRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.IncomeResponse;
import com.expensetracker.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> addIncome(@Valid @RequestBody IncomeRequest request) {
        return ResponseEntity.ok(incomeService.addIncome(request));
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getMyIncome() {
        return ResponseEntity.ok(incomeService.getMyIncome());
    }

    @GetMapping("/{incomeId}")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable String incomeId) {
        return ResponseEntity.ok(incomeService.getIncomeById(incomeId));
    }

    @PutMapping("/{incomeId}")
    public ResponseEntity<IncomeResponse> updateIncome(
            @PathVariable String incomeId,
            @Valid @RequestBody IncomeRequest request
    ) {
        return ResponseEntity.ok(incomeService.updateIncome(incomeId, request));
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<ApiResponse> deleteIncome(@PathVariable String incomeId) {
        return ResponseEntity.ok(incomeService.deleteIncome(incomeId));
    }
}