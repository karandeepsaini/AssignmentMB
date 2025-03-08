package com.example.merabillassignment.models;

import com.example.merabillassignment.enums.PaymentMode;

public class BankPayment extends Payment {
    private String bankName;
    private String transactionReference;

    public BankPayment(double amount, PaymentMode type, String bankName, String transactionReference) {
        super(amount,type);
        this.bankName = bankName;
        this.transactionReference = transactionReference;
    }

    @Override
    public String getPaymentDetails() {
        return "Bank Transfer:" + getAmount() + " | Bank: " + bankName + " | Ref: " + transactionReference;
    }
}

