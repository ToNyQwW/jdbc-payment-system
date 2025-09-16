package com.ToNyQwW.payment.jdbc.entity;

public class CreditCard {

    private int cardId;
    private Account account;
    private String cardNumber;
    private Double creditLimit;
    private double balance;
    private boolean isBlocked;

    public CreditCard(Account account, String cardNumber, Double creditLimit, double balance, boolean isBlocked) {
        this.account = account;
        this.cardNumber = cardNumber;
        this.creditLimit = creditLimit;
        this.balance = balance;
        this.isBlocked = isBlocked;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardId=" + cardId +
                ", account=" + account +
                ", cardNumber='" + cardNumber + '\'' +
                ", creditLimit=" + creditLimit +
                ", balance=" + balance +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
