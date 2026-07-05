package com.expensetracker.service;

import com.expensetracker.dto.request.IncomeRequest;
import com.expensetracker.dto.response.ApiResponse;
import com.expensetracker.dto.response.IncomeResponse;

import java.util.List;

public interface IncomeService {

    IncomeResponse addIncome(IncomeRequest request);

    List<IncomeResponse> getMyIncome();

    IncomeResponse getIncomeById(String incomeId);

    IncomeResponse updateIncome(String incomeId, IncomeRequest request);

    ApiResponse deleteIncome(String incomeId);
}