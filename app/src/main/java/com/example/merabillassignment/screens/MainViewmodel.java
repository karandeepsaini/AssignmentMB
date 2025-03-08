package com.example.merabillassignment.screens;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.merabillassignment.enums.PaymentMode;
import com.example.merabillassignment.models.Ledger;
import com.example.merabillassignment.models.Payment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainViewmodel extends ViewModel {
    private final MutableLiveData<HashMap<PaymentMode, Payment>> paymentList = new MutableLiveData<>(new HashMap<>());

    public LiveData<HashMap<PaymentMode, Payment>> getPaymentList() {
        return paymentList;
    }

    private final MutableLiveData<Ledger> ledger = new MutableLiveData<>(new Ledger());

    public LiveData<Ledger> getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledgerData) {
        ledger.setValue(ledgerData);
        List<Payment> paymentsCopy = new ArrayList<>(ledgerData.getPayments());

        for (Payment payment : paymentsCopy) {
            addPayment(payment);
        }
    }

    public void addPayment(Payment payment) {
        HashMap<PaymentMode, Payment> currentList = paymentList.getValue();
        if (currentList != null) {
            currentList.put(payment.getType(), payment);
            paymentList.setValue(currentList);
            updateLedger();
        }
    }

    public void removePayment(PaymentMode mode) {
        HashMap<PaymentMode, Payment> currentList = paymentList.getValue();
        if (currentList != null) {
            currentList.remove(mode);
            paymentList.setValue(currentList);
            updateLedger();
        }
    }

    public void updateLedger() {
        Ledger currentLedger = ledger.getValue();
        HashMap<PaymentMode, Payment> currentList = paymentList.getValue();
        if (currentLedger != null) {
            currentLedger.clearPayment();
            if (currentList != null) {
                for (Payment payment : currentList.values()) {
                    currentLedger.addPayment(payment);
                }
            }
            ledger.setValue(currentLedger);
        }
    }


    public boolean checkIfPaymentModeExistInLedger(PaymentMode mode) {
        Ledger currentLedger = ledger.getValue();
        return currentLedger != null && currentLedger.isPaymentModeExist(mode);
    }

    public boolean checkIfPaymentModeExist(PaymentMode mode) {
        HashMap<PaymentMode, Payment> currentList = paymentList.getValue();
        return currentList != null && currentList.containsKey(mode);
    }
}
