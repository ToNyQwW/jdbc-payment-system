package com.ToNyQwW.payment.jdbc.entity;

public class Order {

    private int orderId;
    private Client client;
    private Double amount;
    private OrderStatus status;

    public Order(Client client, Double amount, OrderStatus status) {
        this.client = client;
        this.amount = amount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", client=" + client +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
