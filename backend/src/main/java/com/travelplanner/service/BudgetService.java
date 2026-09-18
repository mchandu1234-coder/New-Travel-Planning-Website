package com.travelplanner.service;

import com.travelplanner.dto.BudgetDTOs.*;
import com.travelplanner.entity.*;
import com.travelplanner.exception.BadRequestException;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripCollaboratorRepository tripCollaboratorRepository;
    private final CurrencyService currencyService;
    private final TripService tripService;

    public BudgetService(ExpenseRepository expenseRepository, ExpenseSplitRepository expenseSplitRepository, TripRepository tripRepository, UserRepository userRepository, TripCollaboratorRepository tripCollaboratorRepository, CurrencyService currencyService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripCollaboratorRepository = tripCollaboratorRepository;
        this.currencyService = currencyService;
        this.tripService = tripService;
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getTripExpenses(Long tripId, Long userId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        tripService.validateUserAccess(trip, userId);

        return expenseRepository.findByTripIdOrderByDateDescCreatedAtDesc(tripId).stream()
                .map(ExpenseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponse addExpense(Long userId, CreateExpenseRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        tripService.validateEditorAccess(trip, userId);

        Long payerId = (request.getPaidByUserId() != null) ? request.getPaidByUserId() : userId;
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new ResourceNotFoundException("Payer not found"));

        BigDecimal convertedAmount = currencyService.convert(request.getAmount(), request.getCurrency(), trip.getCurrency());

        Expense expense = Expense.builder()
                .trip(trip)
                .title(request.getTitle().trim())
                .category(request.getCategory())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .convertedAmount(convertedAmount)
                .paidByUser(payer)
                .splitType(request.getSplitType() != null ? request.getSplitType() : Expense.SplitType.EQUAL)
                .receiptUrl(request.getReceiptUrl())
                .date(request.getDate())
                .notes(request.getNotes())
                .build();

        expense = expenseRepository.save(expense);

        List<TripCollaborator> collaborators = tripCollaboratorRepository.findByTripId(trip.getId());
        List<User> participants = collaborators.stream()
                .map(TripCollaborator::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (participants.isEmpty()) {
            participants = List.of(payer);
        }

        List<ExpenseSplit> splits = new ArrayList<>();

        if (request.getSplitType() == Expense.SplitType.EQUAL || request.getCustomSplits() == null || request.getCustomSplits().isEmpty()) {
            BigDecimal share = convertedAmount.divide(BigDecimal.valueOf(participants.size()), 2, RoundingMode.HALF_UP);
            for (User u : participants) {
                boolean isPayer = u.getId().equals(payer.getId());
                splits.add(ExpenseSplit.builder()
                        .expense(expense)
                        .user(u)
                        .amountOwed(share)
                        .isPaid(isPayer)
                        .paidAt(isPayer ? ZonedDateTime.now() : null)
                        .build());
            }
        } else {
            for (CustomSplitDTO cs : request.getCustomSplits()) {
                User u = userRepository.findById(cs.getUserId()).orElse(null);
                if (u != null) {
                    BigDecimal splitAmt = currencyService.convert(cs.getAmount(), request.getCurrency(), trip.getCurrency());
                    boolean isPayer = u.getId().equals(payer.getId());
                    splits.add(ExpenseSplit.builder()
                            .expense(expense)
                            .user(u)
                            .amountOwed(splitAmt)
                            .isPaid(isPayer)
                            .paidAt(isPayer ? ZonedDateTime.now() : null)
                            .build());
                }
            }
        }

        expenseSplitRepository.saveAll(splits);
        expense.setSplits(splits);

        trip.setActualSpend(trip.getActualSpend().add(convertedAmount));
        tripRepository.save(trip);

        return ExpenseResponse.fromEntity(expense);
    }

    @Transactional
    public void deleteExpense(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        Trip trip = expense.getTrip();
        tripService.validateEditorAccess(trip, userId);

        trip.setActualSpend(trip.getActualSpend().subtract(expense.getConvertedAmount()).max(BigDecimal.ZERO));
        tripRepository.save(trip);

        expenseRepository.delete(expense);
    }

    @Transactional(readOnly = true)
    public BudgetSummaryDTO getBudgetSummary(Long tripId, Long userId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        tripService.validateUserAccess(trip, userId);

        List<Expense> expenses = expenseRepository.findByTripIdOrderByDateDescCreatedAtDesc(tripId);

        BigDecimal totalSpent = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Expense.ExpenseCategory cat : Expense.ExpenseCategory.values()) {
            categoryTotals.put(cat.name(), BigDecimal.ZERO);
        }

        for (Expense exp : expenses) {
            BigDecimal amt = exp.getConvertedAmount();
            totalSpent = totalSpent.add(amt);
            String catName = exp.getCategory().name();
            categoryTotals.put(catName, categoryTotals.getOrDefault(catName, BigDecimal.ZERO).add(amt));
        }

        Map<String, BigDecimal> categoryPercentages = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            if (totalSpent.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal pct = entry.getValue().multiply(BigDecimal.valueOf(100)).divide(totalSpent, 1, RoundingMode.HALF_UP);
                categoryPercentages.put(entry.getKey(), pct);
            } else {
                categoryPercentages.put(entry.getKey(), BigDecimal.ZERO);
            }
        }

        BigDecimal remaining = trip.getTargetBudget().subtract(totalSpent);

        List<ExpenseSplit> allSplits = expenseSplitRepository.findAllByTripId(tripId);
        List<TripCollaborator> collabs = tripCollaboratorRepository.findByTripId(tripId);

        Map<Long, User> userMap = new HashMap<>();
        Map<Long, BigDecimal> paidMap = new HashMap<>();
        Map<Long, BigDecimal> shareMap = new HashMap<>();

        for (TripCollaborator c : collabs) {
            if (c.getUser() != null) {
                userMap.put(c.getUser().getId(), c.getUser());
                paidMap.put(c.getUser().getId(), BigDecimal.ZERO);
                shareMap.put(c.getUser().getId(), BigDecimal.ZERO);
            }
        }

        for (Expense exp : expenses) {
            Long pId = exp.getPaidByUser().getId();
            userMap.putIfAbsent(pId, exp.getPaidByUser());
            paidMap.put(pId, paidMap.getOrDefault(pId, BigDecimal.ZERO).add(exp.getConvertedAmount()));
        }

        for (ExpenseSplit split : allSplits) {
            Long uId = split.getUser().getId();
            userMap.putIfAbsent(uId, split.getUser());
            shareMap.put(uId, shareMap.getOrDefault(uId, BigDecimal.ZERO).add(split.getAmountOwed()));
        }

        List<UserBalanceDTO> userBalances = new ArrayList<>();
        Map<Long, BigDecimal> netBalances = new HashMap<>();

        for (Long uId : userMap.keySet()) {
            User u = userMap.get(uId);
            BigDecimal paid = paidMap.getOrDefault(uId, BigDecimal.ZERO);
            BigDecimal share = shareMap.getOrDefault(uId, BigDecimal.ZERO);
            BigDecimal net = paid.subtract(share);
            netBalances.put(uId, net);

            userBalances.add(UserBalanceDTO.builder()
                    .userId(u.getId())
                    .userName(u.getFullName())
                    .userAvatar(u.getAvatarUrl())
                    .totalPaid(paid)
                    .totalShare(share)
                    .netBalance(net)
                    .build());
        }

        List<DebtSettlementDTO> debts = calculateSimplifiedDebts(netBalances, userMap, trip.getCurrency());

        return BudgetSummaryDTO.builder()
                .targetBudget(trip.getTargetBudget())
                .totalSpent(totalSpent)
                .remainingBudget(remaining)
                .currency(trip.getCurrency())
                .categoryBreakdown(categoryTotals)
                .categoryPercentages(categoryPercentages)
                .debts(debts)
                .userBalances(userBalances)
                .build();
    }

    private List<DebtSettlementDTO> calculateSimplifiedDebts(Map<Long, BigDecimal> netBalances, Map<Long, User> userMap, String currency) {
        List<DebtSettlementDTO> results = new ArrayList<>();

        List<Map.Entry<Long, BigDecimal>> debtors = new ArrayList<>();
        List<Map.Entry<Long, BigDecimal>> creditors = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : netBalances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.valueOf(-0.01)) < 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().abs()));
            } else if (entry.getValue().compareTo(BigDecimal.valueOf(0.01)) > 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        int dIdx = 0, cIdx = 0;
        while (dIdx < debtors.size() && cIdx < creditors.size()) {
            Map.Entry<Long, BigDecimal> debtor = debtors.get(dIdx);
            Map.Entry<Long, BigDecimal> creditor = creditors.get(cIdx);

            BigDecimal dAmount = debtor.getValue();
            BigDecimal cAmount = creditor.getValue();

            BigDecimal settleAmount = dAmount.min(cAmount).setScale(2, RoundingMode.HALF_UP);

            User fromUser = userMap.get(debtor.getKey());
            User toUser = userMap.get(creditor.getKey());

            if (settleAmount.compareTo(BigDecimal.ZERO) > 0 && fromUser != null && toUser != null) {
                results.add(DebtSettlementDTO.builder()
                        .fromUserId(fromUser.getId())
                        .fromUserName(fromUser.getFullName())
                        .fromUserAvatar(fromUser.getAvatarUrl())
                        .toUserId(toUser.getId())
                        .toUserName(toUser.getFullName())
                        .toUserAvatar(toUser.getAvatarUrl())
                        .amount(settleAmount)
                        .currency(currency)
                        .summaryText(fromUser.getFullName() + " owes " + toUser.getFullName() + " " + currency + " " + settleAmount)
                        .build());
            }

            debtor.setValue(dAmount.subtract(settleAmount));
            creditor.setValue(cAmount.subtract(settleAmount));

            if (debtor.getValue().compareTo(BigDecimal.valueOf(0.01)) <= 0) dIdx++;
            if (creditor.getValue().compareTo(BigDecimal.valueOf(0.01)) <= 0) cIdx++;
        }

        return results;
    }

    @Transactional
    public void settleDebt(Long tripId, Long fromUserId, Long toUserId, Long requestingUserId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        tripService.validateEditorAccess(trip, requestingUserId);

        List<ExpenseSplit> unpaidSplits = expenseSplitRepository.findAllByTripId(tripId).stream()
                .filter(s -> s.getUser().getId().equals(fromUserId) && !s.isPaid())
                .collect(Collectors.toList());

        for (ExpenseSplit s : unpaidSplits) {
            if (s.getExpense().getPaidByUser().getId().equals(toUserId)) {
                s.setPaid(true);
                s.setPaidAt(ZonedDateTime.now());
                expenseSplitRepository.save(s);
            }
        }
    }
}
