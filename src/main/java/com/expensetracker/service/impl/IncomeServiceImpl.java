package com.expensetracker.service.impl;

import com.expensetracker.dto.request.IncomeRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.IncomeResponse;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.model.Income;
import com.expensetracker.repository.IncomeRepository;
import com.expensetracker.security.UserPrincipal;
import com.expensetracker.service.IncomeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeServiceImpl(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    public IncomeResponse addIncome(IncomeRequest request) {

        String userId = getCurrentUserId();

        Income income = new Income();
        income.setUserId(userId);
        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setCategory(request.getCategory());
        income.setNote(request.getNote());
        income.setIncomeDate(request.getIncomeDate());
        income.setCreatedAt(LocalDateTime.now());
        income.setUpdatedAt(LocalDateTime.now());

        Income savedIncome = incomeRepository.save(income);

        return mapToResponse(savedIncome);
    }

    @Override
    public List<IncomeResponse> getMyIncome() {

        String userId = getCurrentUserId();

        return incomeRepository.findByUserIdOrderByIncomeDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public IncomeResponse getIncomeById(String incomeId) {

        String userId = getCurrentUserId();

        Income income = incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        return mapToResponse(income);
    }

    @Override
    public IncomeResponse updateIncome(String incomeId, IncomeRequest request) {

        String userId = getCurrentUserId();

        Income income = incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setCategory(request.getCategory());
        income.setNote(request.getNote());
        income.setIncomeDate(request.getIncomeDate());
        income.setUpdatedAt(LocalDateTime.now());

        Income updatedIncome = incomeRepository.save(income);

        return mapToResponse(updatedIncome);
    }

    @Override
    public ApiResponse deleteIncome(String incomeId) {

        String userId = getCurrentUserId();

        Income income = incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        incomeRepository.delete(income);

        return new ApiResponse(true, "Income deleted successfully");
    }

    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userPrincipal.getId();
    }

    private IncomeResponse mapToResponse(Income income) {

        return new IncomeResponse(
                income.getId(),
                income.getTitle(),
                income.getAmount(),
                income.getCategory(),
                income.getNote(),
                income.getIncomeDate(),
                income.getCreatedAt(),
                income.getUpdatedAt()
        );
    }
}