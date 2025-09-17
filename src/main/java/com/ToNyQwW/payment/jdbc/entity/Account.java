package com.ToNyQwW.payment.jdbc.entity;

public class Account {

    private int accountId;
    private Client client;
    private double balance;
    private boolean isActive;

    public Account(int accountId, Client client, double balance, boolean isActive) {
        this.accountId = accountId;
        this.client = client;
        this.balance = balance;
        this.isActive = isActive;
    }

    public Account(Client client, double balance, boolean isActive) {
        this.client = client;
        this.balance = balance;
        this.isActive = isActive;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", client=" + client +
                ", balance=" + balance +
                ", isActive=" + isActive +
                '}';
    }
}
