package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expenses")
public class Expense {

    public enum ExpenseCategory {
        STAYS,
        FLIGHTS,
        FOOD_DRINK,
        ACTIVITIES,
        TRANSPORT,
        SHOPPING,
        MISC;

        @com.fasterxml.jackson.annotation.JsonCreator
        public static ExpenseCategory fromString(String value) {
            if (value == null) return MISC;
            String upper = value.trim().toUpperCase();
            if (upper.equals("FOOD") || upper.equals("FOOD_DRINK") || upper.equals("DINING")) return FOOD_DRINK;
            if (upper.equals("HOTEL") || upper.equals("STAYS") || upper.equals("ACCOMMODATION")) return STAYS;
            if (upper.equals("FLIGHT") || upper.equals("FLIGHTS")) return FLIGHTS;
            if (upper.equals("ACTIVITY") || upper.equals("ACTIVITIES") || upper.equals("SIGHTSEEING")) return ACTIVITIES;
            if (upper.equals("TRANSPORT") || upper.equals("TRANSIT")) return TRANSPORT;
            if (upper.equals("SHOPPING")) return SHOPPING;
            try {
                return ExpenseCategory.valueOf(upper);
            } catch (Exception e) {
                return MISC;
            }
        }
    }

    public enum SplitType {
        EQUAL,
        EXACT,
        PERCENTAGE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExpenseCategory category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency = "USD";

    @Column(name = "converted_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal convertedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User paidByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type", length = 50)
    private SplitType splitType = SplitType.EQUAL;

    @Column(name = "receipt_url", columnDefinition = "TEXT")
    private String receiptUrl;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseSplit> splits = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public Expense() {}

    public Expense(Long id, Trip trip, String title, ExpenseCategory category, BigDecimal amount, String currency, BigDecimal convertedAmount, User paidByUser, SplitType splitType, String receiptUrl, LocalDate date, String notes, List<ExpenseSplit> splits, ZonedDateTime createdAt) {
        this.id = id;
        this.trip = trip;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.currency = currency != null ? currency : "USD";
        this.convertedAmount = convertedAmount;
        this.paidByUser = paidByUser;
        this.splitType = splitType != null ? splitType : SplitType.EQUAL;
        this.receiptUrl = receiptUrl;
        this.date = date;
        this.notes = notes;
        this.splits = splits != null ? splits : new ArrayList<>();
        this.createdAt = createdAt;
    }

    public static ExpenseBuilder builder() {
        return new ExpenseBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }
    public User getPaidByUser() { return paidByUser; }
    public void setPaidByUser(User paidByUser) { this.paidByUser = paidByUser; }
    public SplitType getSplitType() { return splitType; }
    public void setSplitType(SplitType splitType) { this.splitType = splitType; }
    public String getReceiptUrl() { return receiptUrl; }
    public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<ExpenseSplit> getSplits() { return splits; }
    public void setSplits(List<ExpenseSplit> splits) { this.splits = splits; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public static class ExpenseBuilder {
        private Long id;
        private Trip trip;
        private String title;
        private ExpenseCategory category;
        private BigDecimal amount;
        private String currency = "USD";
        private BigDecimal convertedAmount;
        private User paidByUser;
        private SplitType splitType = SplitType.EQUAL;
        private String receiptUrl;
        private LocalDate date;
        private String notes;
        private List<ExpenseSplit> splits = new ArrayList<>();
        private ZonedDateTime createdAt;

        public ExpenseBuilder id(Long id) { this.id = id; return this; }
        public ExpenseBuilder trip(Trip trip) { this.trip = trip; return this; }
        public ExpenseBuilder title(String title) { this.title = title; return this; }
        public ExpenseBuilder category(ExpenseCategory category) { this.category = category; return this; }
        public ExpenseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public ExpenseBuilder currency(String currency) { this.currency = currency; return this; }
        public ExpenseBuilder convertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; return this; }
        public ExpenseBuilder paidByUser(User paidByUser) { this.paidByUser = paidByUser; return this; }
        public ExpenseBuilder splitType(SplitType splitType) { this.splitType = splitType; return this; }
        public ExpenseBuilder receiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
        public ExpenseBuilder date(LocalDate date) { this.date = date; return this; }
        public ExpenseBuilder notes(String notes) { this.notes = notes; return this; }
        public ExpenseBuilder splits(List<ExpenseSplit> splits) { this.splits = splits; return this; }
        public ExpenseBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Expense build() {
            return new Expense(id, trip, title, category, amount, currency, convertedAmount, paidByUser, splitType, receiptUrl, date, notes, splits, createdAt);
        }
    }
}
