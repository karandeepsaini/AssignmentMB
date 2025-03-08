package com.example.merabillassignment.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.merabillassignment.R;
import com.example.merabillassignment.databinding.FragmentAddPaymentDialogBinding;
import com.example.merabillassignment.enums.PaymentMode;
import com.example.merabillassignment.models.BankPayment;
import com.example.merabillassignment.models.CashPayment;
import com.example.merabillassignment.models.CreditCardPayment;
import com.example.merabillassignment.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class AddPaymentDialog extends DialogFragment {

    private FragmentAddPaymentDialogBinding binding;
    private MainViewmodel mainViewmodel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPaymentDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 23) {
            binding.spinnerPaymentType.setBackgroundResource(R.drawable.spinner_shape);
        }
        mainViewmodel = new ViewModelProvider(requireActivity()).get(MainViewmodel.class);


        mainViewmodel.getLedger().observe(getViewLifecycleOwner(), ledger -> {
            if (ledger != null) {

            }
        });

        List<String> paymentMode = new ArrayList<>();
        for (PaymentMode mode : PaymentMode.values()) {
            if (!mainViewmodel.checkIfPaymentModeExist(mode)) {
                paymentMode.add(mode.getValue());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, paymentMode);
        binding.spinnerPaymentType.setAdapter(adapter);


        binding.spinnerPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPayment = parent.getItemAtPosition(position).toString();

                if (selectedPayment.equals(PaymentMode.BANK.getValue()) || selectedPayment.equals(PaymentMode.CREDIT.getValue())) {
                    binding.etBankName.setVisibility(View.VISIBLE);
                    binding.etTransactionId.setVisibility(View.VISIBLE);
                } else {
                    binding.etBankName.setVisibility(View.GONE);
                    binding.etTransactionId.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnOK.setOnClickListener(v -> {
            processPayment();
        });


        binding.btnCancel.setOnClickListener(v -> dismiss());
    }


    private void processPayment() {
        String paymentType = binding.spinnerPaymentType.getSelectedItem().toString();
        String amountTv = binding.etAmount.getText().toString().trim();
        String bankName = binding.etBankName.getText().toString().trim();
        String transactionRef = binding.etTransactionId.getText().toString().trim();
        Payment payment;

        PaymentMode method = PaymentMode.fromString(paymentType);

        if (amountTv.isEmpty()) {
            Toast.makeText(getContext(), "Amount is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountTv);

        double remainingAmount = mainViewmodel.getLedger().getValue().getRemainingAmount();
        if (amount > remainingAmount) {
            Toast.makeText(getContext(), "Payment exceeds remaining balance! Remaining: Rs " + remainingAmount, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (method) {
            case CASH:
                payment = new CashPayment(amount, PaymentMode.CASH);
                break;

            case BANK:
                if (bankName.isEmpty() || transactionRef.isEmpty()) {
                    Toast.makeText(getContext(), "Amount, Bank Name and Transaction ID are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                payment = new BankPayment(amount, PaymentMode.BANK, bankName, transactionRef);
                break;

            case CREDIT:
                if (bankName.isEmpty() || transactionRef.isEmpty()) {
                    Toast.makeText(getContext(), "Amount, Bank Name and Transaction ID are required for Credit Card Payment!", Toast.LENGTH_SHORT).show();
                    return;
                }

                payment = new CreditCardPayment(amount, PaymentMode.CREDIT, bankName, transactionRef);
                break;

            default:
                Toast.makeText(getContext(), "Invalid Payment Type", Toast.LENGTH_SHORT).show();
                return;
        }


        mainViewmodel.addPayment(payment);


        dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        Window window = requireDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        window.setLayout(width, height);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}