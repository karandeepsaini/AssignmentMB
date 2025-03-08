package com.example.merabillassignment.models;

import com.example.merabillassignment.enums.PaymentMode;

public class CreditCardPayment extends Payment {
    private String bankName;
    private String transactionReference;

    public CreditCardPayment(double amount, PaymentMode type,String bankName, String transactionReference) {
        super(amount, type);
        this.bankName = bankName;
        this.transactionReference = transactionReference;
    }


    @Override
    public String getPaymentDetails() {
        return "Credit Card Payment: " + getAmount() + " | Bank: " + bankName + " | Ref: " + transactionReference;
    }
}

