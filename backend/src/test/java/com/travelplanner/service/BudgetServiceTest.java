package com.travelplanner.service;

import com.travelplanner.dto.BudgetDTOs.*;
import com.travelplanner.entity.*;
import com.travelplanner.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseSplitRepository expenseSplitRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TripCollaboratorRepository tripCollaboratorRepository;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private TripService tripService;

    @InjectMocks
    private BudgetService budgetService;

    private User userAlex;
    private User userSarah;
    private Trip sampleTrip;

    @BeforeEach
    void setUp() {
        userAlex = User.builder().id(1L).email("alex@example.com").fullName("Alex").build();
        userSarah = User.builder().id(2L).email("sarah@example.com").fullName("Sarah").build();

        sampleTrip = Trip.builder()
                .id(100L)
                .owner(userAlex)
                .title("Trip to Paris")
                .targetBudget(BigDecimal.valueOf(1000))
                .actualSpend(BigDecimal.ZERO)
                .currency("USD")
                .build();
    }

    @Test
    void addExpense_EqualSplit_Success() {
        CreateExpenseRequest request = CreateExpenseRequest.builder()
                .tripId(100L)
                .title("Dinner")
                .category(Expense.ExpenseCategory.FOOD_DRINK)
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .date(LocalDate.now())
                .splitType(Expense.SplitType.EQUAL)
                .build();

        TripCollaborator collabAlex = TripCollaborator.builder().user(userAlex).build();
        TripCollaborator collabSarah = TripCollaborator.builder().user(userSarah).build();

        when(tripRepository.findByIdAndIsDeletedFalse(100L)).thenReturn(Optional.of(sampleTrip));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userAlex));
        when(currencyService.convert(any(), any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(i -> {
            Expense e = (Expense) i.getArgument(0);
            e.setId(1L);
            return e;
        });
        when(tripCollaboratorRepository.findByTripId(100L)).thenReturn(List.of(collabAlex, collabSarah));

        ExpenseResponse response = budgetService.addExpense(1L, request);

        assertNotNull(response);
        assertEquals("Dinner", response.getTitle());
        verify(expenseSplitRepository, times(1)).saveAll(any());
        assertEquals(BigDecimal.valueOf(100), sampleTrip.getActualSpend());
    }
}
