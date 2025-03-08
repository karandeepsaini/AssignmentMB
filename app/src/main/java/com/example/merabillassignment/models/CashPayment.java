package com.example.merabillassignment.models;

import com.example.merabillassignment.enums.PaymentMode;

public class CashPayment extends Payment {


    public CashPayment(double amount, PaymentMode type) {
        super(amount, type);
    }

    @Override
    public String getPaymentDetails() {
        return "Cash Payment: " + getAmount();
    }
}

