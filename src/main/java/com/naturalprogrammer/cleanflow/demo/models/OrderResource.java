package com.naturalprogrammer.cleanflow.demo.models;

public class OrderResource {

    public Integer customerId;
    public Integer productId;

    @Override
    public String toString() {
        return "OrderResource{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                '}';
    }
}
