package com.travelplanner.dto;

import com.travelplanner.entity.Expense;
import com.travelplanner.entity.ExpenseSplit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetDTOs {

    public static class CreateExpenseRequest {
        @NotNull
        private Long tripId;
        @NotBlank
        private String title;
        @NotNull
        private Expense.ExpenseCategory category;
        @NotNull
        private BigDecimal amount;
        private String currency = "USD";
        private LocalDate date;
        private Long paidByUserId;
        private Expense.SplitType splitType = Expense.SplitType.EQUAL;
        private String notes;
        private String receiptUrl;
        private List<CustomSplitDTO> customSplits;

        public CreateExpenseRequest() {}

        public static CreateExpenseRequestBuilder builder() { return new CreateExpenseRequestBuilder(); }

        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Expense.ExpenseCategory getCategory() { return category; }
        public void setCategory(Expense.ExpenseCategory category) { this.category = category; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency != null ? currency : "USD"; }
        public void setCurrency(String currency) { this.currency = currency; }
        public LocalDate getDate() { return date != null ? date : LocalDate.now(); }
        public void setDate(LocalDate date) { this.date = date; }
        public Long getPaidByUserId() { return paidByUserId; }
        public void setPaidByUserId(Long paidByUserId) { this.paidByUserId = paidByUserId; }
        public void setPaidBy(String paidBy) {
            try {
                this.paidByUserId = Long.parseLong(paidBy);
            } catch (Exception ignored) {}
        }
        public Expense.SplitType getSplitType() { return splitType != null ? splitType : Expense.SplitType.EQUAL; }
        public void setSplitType(Expense.SplitType splitType) { this.splitType = splitType; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getReceiptUrl() { return receiptUrl; }
        public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }
        public List<CustomSplitDTO> getCustomSplits() { return customSplits; }
        public void setCustomSplits(List<CustomSplitDTO> customSplits) { this.customSplits = customSplits; }

        public static class CreateExpenseRequestBuilder {
            private Long tripId;
            private String title;
            private Expense.ExpenseCategory category;
            private BigDecimal amount;
            private String currency = "USD";
            private LocalDate date;
            private Long paidByUserId;
            private Expense.SplitType splitType = Expense.SplitType.EQUAL;
            private String notes;
            private String receiptUrl;
            private List<CustomSplitDTO> customSplits;

            public CreateExpenseRequestBuilder tripId(Long tripId) { this.tripId = tripId; return this; }
            public CreateExpenseRequestBuilder title(String title) { this.title = title; return this; }
            public CreateExpenseRequestBuilder category(Expense.ExpenseCategory category) { this.category = category; return this; }
            public CreateExpenseRequestBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
            public CreateExpenseRequestBuilder currency(String currency) { this.currency = currency; return this; }
            public CreateExpenseRequestBuilder date(LocalDate date) { this.date = date; return this; }
            public CreateExpenseRequestBuilder paidByUserId(Long paidByUserId) { this.paidByUserId = paidByUserId; return this; }
            public CreateExpenseRequestBuilder splitType(Expense.SplitType splitType) { this.splitType = splitType; return this; }
            public CreateExpenseRequestBuilder notes(String notes) { this.notes = notes; return this; }
            public CreateExpenseRequestBuilder receiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
            public CreateExpenseRequestBuilder customSplits(List<CustomSplitDTO> customSplits) { this.customSplits = customSplits; return this; }

            public CreateExpenseRequest build() {
                CreateExpenseRequest r = new CreateExpenseRequest();
                r.tripId = tripId; r.title = title; r.category = category; r.amount = amount;
                r.currency = currency; r.date = date; r.paidByUserId = paidByUserId;
                r.splitType = splitType; r.notes = notes; r.receiptUrl = receiptUrl;
                r.customSplits = customSplits;
                return r;
            }
        }
    }

    public static class CustomSplitDTO {
        private Long userId;
        private BigDecimal amount;

        public CustomSplitDTO() {}
        public CustomSplitDTO(Long userId, BigDecimal amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    public static class ExpenseResponse {
        private Long id;
        private Long tripId;
        private String title;
        private Expense.ExpenseCategory category;
        private BigDecimal amount;
        private String currency;
        private BigDecimal convertedAmount;
        private AuthDTOs.UserSummaryDTO paidBy;
        private Expense.SplitType splitType;
        private String receiptUrl;
        private LocalDate date;
        private String notes;
        private ZonedDateTime createdAt;
        private List<ExpenseSplitResponse> splits;

        public ExpenseResponse() {}

        public static ExpenseResponse fromEntity(Expense e) {
            if (e == null) return null;
            ExpenseResponse r = new ExpenseResponse();
            r.id = e.getId();
            r.tripId = e.getTrip() != null ? e.getTrip().getId() : null;
            r.title = e.getTitle();
            r.category = e.getCategory();
            r.amount = e.getAmount();
            r.currency = e.getCurrency();
            r.convertedAmount = e.getConvertedAmount();
            r.paidBy = AuthDTOs.UserSummaryDTO.fromEntity(e.getPaidByUser());
            r.splitType = e.getSplitType();
            r.receiptUrl = e.getReceiptUrl();
            r.date = e.getDate();
            r.notes = e.getNotes();
            r.createdAt = e.getCreatedAt();
            r.splits = e.getSplits() != null ?
                    e.getSplits().stream().map(ExpenseSplitResponse::fromEntity).collect(Collectors.toList()) :
                    List.of();
            return r;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Expense.ExpenseCategory getCategory() { return category; }
        public void setCategory(Expense.ExpenseCategory category) { this.category = category; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public BigDecimal getConvertedAmount() { return convertedAmount; }
        public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }
        public AuthDTOs.UserSummaryDTO getPaidBy() { return paidBy; }
        public void setPaidBy(AuthDTOs.UserSummaryDTO paidBy) { this.paidBy = paidBy; }
        public Expense.SplitType getSplitType() { return splitType; }
        public void setSplitType(Expense.SplitType splitType) { this.splitType = splitType; }
        public String getReceiptUrl() { return receiptUrl; }
        public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public ZonedDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
        public List<ExpenseSplitResponse> getSplits() { return splits; }
        public void setSplits(List<ExpenseSplitResponse> splits) { this.splits = splits; }
    }

    public static class ExpenseSplitResponse {
        private Long id;
        private AuthDTOs.UserSummaryDTO user;
        private BigDecimal amountOwed;
        private boolean isPaid;
        private ZonedDateTime paidAt;

        public ExpenseSplitResponse() {}

        public static ExpenseSplitResponse fromEntity(ExpenseSplit s) {
            if (s == null) return null;
            ExpenseSplitResponse r = new ExpenseSplitResponse();
            r.id = s.getId();
            r.user = AuthDTOs.UserSummaryDTO.fromEntity(s.getUser());
            r.amountOwed = s.getAmountOwed();
            r.isPaid = s.isPaid();
            r.paidAt = s.getPaidAt();
            return r;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public AuthDTOs.UserSummaryDTO getUser() { return user; }
        public void setUser(AuthDTOs.UserSummaryDTO user) { this.user = user; }
        public BigDecimal getAmountOwed() { return amountOwed; }
        public void setAmountOwed(BigDecimal amountOwed) { this.amountOwed = amountOwed; }
        public boolean isPaid() { return isPaid; }
        public void setPaid(boolean paid) { isPaid = paid; }
        public ZonedDateTime getPaidAt() { return paidAt; }
        public void setPaidAt(ZonedDateTime paidAt) { this.paidAt = paidAt; }
    }

    public static class BudgetSummaryDTO {
        private BigDecimal targetBudget;
        private BigDecimal totalSpent;
        private BigDecimal remainingBudget;
        private String currency;
        private Map<String, BigDecimal> categoryBreakdown;
        private Map<String, BigDecimal> categoryPercentages;
        private List<DebtSettlementDTO> debts;
        private List<UserBalanceDTO> userBalances;

        public BudgetSummaryDTO() {}
        public static BudgetSummaryDTOBuilder builder() { return new BudgetSummaryDTOBuilder(); }

        public BigDecimal getTargetBudget() { return targetBudget != null ? targetBudget : BigDecimal.ZERO; }
        public void setTargetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; }
        public BigDecimal getTotalBudget() { return targetBudget != null ? targetBudget : BigDecimal.ZERO; }
        public void setTotalBudget(BigDecimal totalBudget) { this.targetBudget = totalBudget; }
        public BigDecimal getTotalSpent() { return totalSpent; }
        public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
        public BigDecimal getRemainingBudget() { return remainingBudget; }
        public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Map<String, BigDecimal> getCategoryBreakdown() { return categoryBreakdown; }
        public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
        public Map<String, BigDecimal> getCategoryPercentages() { return categoryPercentages; }
        public void setCategoryPercentages(Map<String, BigDecimal> categoryPercentages) { this.categoryPercentages = categoryPercentages; }
        public List<DebtSettlementDTO> getDebts() { return debts; }
        public void setDebts(List<DebtSettlementDTO> debts) { this.debts = debts; }
        public List<UserBalanceDTO> getUserBalances() { return userBalances; }
        public void setUserBalances(List<UserBalanceDTO> userBalances) { this.userBalances = userBalances; }

        public static class BudgetSummaryDTOBuilder {
            private BigDecimal targetBudget;
            private BigDecimal totalSpent;
            private BigDecimal remainingBudget;
            private String currency;
            private Map<String, BigDecimal> categoryBreakdown;
            private Map<String, BigDecimal> categoryPercentages;
            private List<DebtSettlementDTO> debts;
            private List<UserBalanceDTO> userBalances;

            public BudgetSummaryDTOBuilder targetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; return this; }
            public BudgetSummaryDTOBuilder totalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; return this; }
            public BudgetSummaryDTOBuilder remainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; return this; }
            public BudgetSummaryDTOBuilder currency(String currency) { this.currency = currency; return this; }
            public BudgetSummaryDTOBuilder categoryBreakdown(Map<String, BigDecimal> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; return this; }
            public BudgetSummaryDTOBuilder categoryPercentages(Map<String, BigDecimal> categoryPercentages) { this.categoryPercentages = categoryPercentages; return this; }
            public BudgetSummaryDTOBuilder debts(List<DebtSettlementDTO> debts) { this.debts = debts; return this; }
            public BudgetSummaryDTOBuilder userBalances(List<UserBalanceDTO> userBalances) { this.userBalances = userBalances; return this; }

            public BudgetSummaryDTO build() {
                BudgetSummaryDTO dto = new BudgetSummaryDTO();
                dto.targetBudget = targetBudget; dto.totalSpent = totalSpent; dto.remainingBudget = remainingBudget;
                dto.currency = currency; dto.categoryBreakdown = categoryBreakdown; dto.categoryPercentages = categoryPercentages;
                dto.debts = debts; dto.userBalances = userBalances;
                return dto;
            }
        }
    }

    public static class UserBalanceDTO {
        private Long userId;
        private String userName;
        private String userAvatar;
        private BigDecimal totalPaid;
        private BigDecimal totalShare;
        private BigDecimal netBalance;

        public UserBalanceDTO() {}
        public static UserBalanceDTOBuilder builder() { return new UserBalanceDTOBuilder(); }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getUserAvatar() { return userAvatar; }
        public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }
        public BigDecimal getTotalPaid() { return totalPaid; }
        public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }
        public BigDecimal getTotalShare() { return totalShare; }
        public void setTotalShare(BigDecimal totalShare) { this.totalShare = totalShare; }
        public BigDecimal getNetBalance() { return netBalance; }
        public void setNetBalance(BigDecimal netBalance) { this.netBalance = netBalance; }

        public static class UserBalanceDTOBuilder {
            private Long userId;
            private String userName;
            private String userAvatar;
            private BigDecimal totalPaid;
            private BigDecimal totalShare;
            private BigDecimal netBalance;

            public UserBalanceDTOBuilder userId(Long userId) { this.userId = userId; return this; }
            public UserBalanceDTOBuilder userName(String userName) { this.userName = userName; return this; }
            public UserBalanceDTOBuilder userAvatar(String userAvatar) { this.userAvatar = userAvatar; return this; }
            public UserBalanceDTOBuilder totalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; return this; }
            public UserBalanceDTOBuilder totalShare(BigDecimal totalShare) { this.totalShare = totalShare; return this; }
            public UserBalanceDTOBuilder netBalance(BigDecimal netBalance) { this.netBalance = netBalance; return this; }

            public UserBalanceDTO build() {
                UserBalanceDTO b = new UserBalanceDTO();
                b.userId = userId; b.userName = userName; b.userAvatar = userAvatar;
                b.totalPaid = totalPaid; b.totalShare = totalShare; b.netBalance = netBalance;
                return b;
            }
        }
    }

    public static class DebtSettlementDTO {
        private Long fromUserId;
        private String fromUserName;
        private String fromUserAvatar;
        private Long toUserId;
        private String toUserName;
        private String toUserAvatar;
        private BigDecimal amount;
        private String currency;
        private String summaryText;

        public DebtSettlementDTO() {}
        public static DebtSettlementDTOBuilder builder() { return new DebtSettlementDTOBuilder(); }

        public Long getFromUserId() { return fromUserId; }
        public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
        public String getFromUserName() { return fromUserName; }
        public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
        public String getFromUserAvatar() { return fromUserAvatar; }
        public void setFromUserAvatar(String fromUserAvatar) { this.fromUserAvatar = fromUserAvatar; }
        public Long getToUserId() { return toUserId; }
        public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
        public String getToUserName() { return toUserName; }
        public void setToUserName(String toUserName) { this.toUserName = toUserName; }
        public String getToUserAvatar() { return toUserAvatar; }
        public void setToUserAvatar(String toUserAvatar) { this.toUserAvatar = toUserAvatar; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getSummaryText() { return summaryText; }
        public void setSummaryText(String summaryText) { this.summaryText = summaryText; }

        public static class DebtSettlementDTOBuilder {
            private Long fromUserId;
            private String fromUserName;
            private String fromUserAvatar;
            private Long toUserId;
            private String toUserName;
            private String toUserAvatar;
            private BigDecimal amount;
            private String currency;
            private String summaryText;

            public DebtSettlementDTOBuilder fromUserId(Long fromUserId) { this.fromUserId = fromUserId; return this; }
            public DebtSettlementDTOBuilder fromUserName(String fromUserName) { this.fromUserName = fromUserName; return this; }
            public DebtSettlementDTOBuilder fromUserAvatar(String fromUserAvatar) { this.fromUserAvatar = fromUserAvatar; return this; }
            public DebtSettlementDTOBuilder toUserId(Long toUserId) { this.toUserId = toUserId; return this; }
            public DebtSettlementDTOBuilder toUserName(String toUserName) { this.toUserName = toUserName; return this; }
            public DebtSettlementDTOBuilder toUserAvatar(String toUserAvatar) { this.toUserAvatar = toUserAvatar; return this; }
            public DebtSettlementDTOBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
            public DebtSettlementDTOBuilder currency(String currency) { this.currency = currency; return this; }
            public DebtSettlementDTOBuilder summaryText(String summaryText) { this.summaryText = summaryText; return this; }

            public DebtSettlementDTO build() {
                DebtSettlementDTO d = new DebtSettlementDTO();
                d.fromUserId = fromUserId; d.fromUserName = fromUserName; d.fromUserAvatar = fromUserAvatar;
                d.toUserId = toUserId; d.toUserName = toUserName; d.toUserAvatar = toUserAvatar;
                d.amount = amount; d.currency = currency; d.summaryText = summaryText;
                return d;
            }
        }
    }

    public static class ExchangeRatesDTO {
        private String baseCurrency;
        private Map<String, BigDecimal> rates;
        private String lastUpdated;

        public ExchangeRatesDTO() {}
        public static ExchangeRatesDTOBuilder builder() { return new ExchangeRatesDTOBuilder(); }

        public String getBaseCurrency() { return baseCurrency; }
        public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
        public Map<String, BigDecimal> getRates() { return rates; }
        public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }
        public String getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

        public static class ExchangeRatesDTOBuilder {
            private String baseCurrency;
            private Map<String, BigDecimal> rates;
            private String lastUpdated;

            public ExchangeRatesDTOBuilder baseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; return this; }
            public ExchangeRatesDTOBuilder rates(Map<String, BigDecimal> rates) { this.rates = rates; return this; }
            public ExchangeRatesDTOBuilder lastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; return this; }

            public ExchangeRatesDTO build() {
                ExchangeRatesDTO dto = new ExchangeRatesDTO();
                dto.baseCurrency = baseCurrency;
                dto.rates = rates;
                dto.lastUpdated = lastUpdated;
                return dto;
            }
        }
    }
}
