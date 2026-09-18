package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "expense_splits")
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    @JsonIgnore
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User user;

    @Column(name = "amount_owed", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountOwed;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid = false;

    @Column(name = "paid_at")
    private ZonedDateTime paidAt;

    public ExpenseSplit() {}

    public ExpenseSplit(Long id, Expense expense, User user, BigDecimal amountOwed, boolean isPaid, ZonedDateTime paidAt) {
        this.id = id;
        this.expense = expense;
        this.user = user;
        this.amountOwed = amountOwed;
        this.isPaid = isPaid;
        this.paidAt = paidAt;
    }

    public static ExpenseSplitBuilder builder() {
        return new ExpenseSplitBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Expense getExpense() { return expense; }
    public void setExpense(Expense expense) { this.expense = expense; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getAmountOwed() { return amountOwed; }
    public void setAmountOwed(BigDecimal amountOwed) { this.amountOwed = amountOwed; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public ZonedDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(ZonedDateTime paidAt) { this.paidAt = paidAt; }

    public static class ExpenseSplitBuilder {
        private Long id;
        private Expense expense;
        private User user;
        private BigDecimal amountOwed;
        private boolean isPaid = false;
        private ZonedDateTime paidAt;

        public ExpenseSplitBuilder id(Long id) { this.id = id; return this; }
        public ExpenseSplitBuilder expense(Expense expense) { this.expense = expense; return this; }
        public ExpenseSplitBuilder user(User user) { this.user = user; return this; }
        public ExpenseSplitBuilder amountOwed(BigDecimal amountOwed) { this.amountOwed = amountOwed; return this; }
        public ExpenseSplitBuilder isPaid(boolean isPaid) { this.isPaid = isPaid; return this; }
        public ExpenseSplitBuilder paidAt(ZonedDateTime paidAt) { this.paidAt = paidAt; return this; }

        public ExpenseSplit build() {
            return new ExpenseSplit(id, expense, user, amountOwed, isPaid, paidAt);
        }
    }
}
