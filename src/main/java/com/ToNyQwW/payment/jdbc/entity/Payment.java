package com.ToNyQwW.payment.jdbc.entity;

import java.time.LocalDateTime;

public class Payment {

    private int paymentId;
    private Account fromAccount;
    private Account toAccount;
    private Order order;
    private Double amount;
    private LocalDateTime createdAt;

    public Payment(int paymentId, Account fromAccount, Account toAccount, Order order, Double amount, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.order = order;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Payment(Account fromAccount, Account toAccount, Order order, Double amount, LocalDateTime createdAt) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.order = order;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", order=" + order +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
