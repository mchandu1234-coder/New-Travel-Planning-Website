package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.BudgetDTOs.*;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.BudgetService;
import com.travelplanner.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
@Tag(name = "Budget & Expenses", description = "Endpoints for expense tracking, multi-currency conversion, and debt settlements")
public class BudgetController {

    private final BudgetService budgetService;
    private final CurrencyService currencyService;

    public BudgetController(BudgetService budgetService, CurrencyService currencyService) {
        this.budgetService = budgetService;
        this.currencyService = currencyService;
    }

    @GetMapping("/trips/{tripId}/expenses")
    @Operation(summary = "Get all expenses logged for a trip")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getTripExpenses(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<ExpenseResponse> expenses = budgetService.getTripExpenses(tripId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(expenses));
    }

    @PostMapping("/expenses")
    @Operation(summary = "Log an expense with category and automatic split calculation")
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse expense = budgetService.addExpense(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Expense recorded", expense));
    }

    @DeleteMapping("/expenses/{expenseId}")
    @Operation(summary = "Delete an expense")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable Long expenseId,
            @AuthenticationPrincipal UserPrincipal principal) {
        budgetService.deleteExpense(expenseId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Expense deleted", null));
    }

    @GetMapping("/trips/{tripId}/summary")
    @Operation(summary = "Get trip budget summary, category analytics, and simplified debt settlement list")
    public ResponseEntity<ApiResponse<BudgetSummaryDTO>> getBudgetSummary(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal) {
        BudgetSummaryDTO summary = budgetService.getBudgetSummary(tripId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @PostMapping("/trips/{tripId}/settle")
    @Operation(summary = "Settle a split debt between collaborators")
    public ResponseEntity<ApiResponse<Void>> settleDebt(
            @PathVariable Long tripId,
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @AuthenticationPrincipal UserPrincipal principal) {
        budgetService.settleDebt(tripId, fromUserId, toUserId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Debt settled successfully", null));
    }

    @GetMapping("/rates")
    @Operation(summary = "Get live currency exchange rates")
    public ResponseEntity<ApiResponse<ExchangeRatesDTO>> getRates(@RequestParam(defaultValue = "USD") String base) {
        ExchangeRatesDTO rates = currencyService.getRates(base);
        return ResponseEntity.ok(ApiResponse.ok(rates));
    }
}
