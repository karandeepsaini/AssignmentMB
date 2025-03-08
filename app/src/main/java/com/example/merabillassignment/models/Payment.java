package com.example.merabillassignment.models;

import com.example.merabillassignment.enums.PaymentMode;

public abstract class Payment {
    private double amount;
    private PaymentMode type;

    public Payment(double amount,PaymentMode type) {
        this.amount = amount;
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMode getType() {
        return type;
    }

    public abstract String getPaymentDetails();
}


