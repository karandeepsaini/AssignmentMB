package com.example.merabillassignment.models;

import com.example.merabillassignment.enums.PaymentMode;

import java.util.ArrayList;
import java.util.List;

public class Ledger {
    private final double totalAmount = 2000;
    private List<Payment> payments = new ArrayList<>();

    public Ledger() {
        this.payments = new ArrayList<>();
    }

    public boolean isPaymentModeExist(PaymentMode mode) {
        for (Payment p : payments) {
            if (p.getType().equals(mode)) {
                return true;
            }
        }
        return false;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void clearPayment(){
        payments.clear();
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public double getRemainingAmount() {
        double remaining = totalAmount;
        for (Payment amount : payments) {
            remaining -= amount.getAmount();
        }
        return remaining;
    }
}
