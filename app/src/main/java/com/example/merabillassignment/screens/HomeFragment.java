package com.example.merabillassignment.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.merabillassignment.R;
import com.example.merabillassignment.databinding.FragmentHomeBinding;
import com.example.merabillassignment.enums.PaymentMode;
import com.example.merabillassignment.utils.FileUtil;
import com.example.merabillassignment.models.Ledger;
import com.example.merabillassignment.models.Payment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NavController navController;
    private MainViewmodel mainViewmodel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewmodel = new ViewModelProvider(requireActivity()).get(MainViewmodel.class);

        Ledger savedLedger = FileUtil.loadLedger(requireContext());
        mainViewmodel.setLedger(savedLedger);


        navController = Navigation.findNavController(requireView());


        binding.saveButton.setOnClickListener(sb -> {
            Ledger currentLedger = mainViewmodel.getLedger().getValue();
            if (currentLedger != null) {
                FileUtil.saveLedger(requireContext(), currentLedger);
                Toast.makeText(requireContext(), "Ledger saved!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addPaymentButton.setOnClickListener(buttonView -> {
            NavController navController = Navigation.findNavController(buttonView);

            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.addPaymentDialog) {
                return;
            }

            NavDirections directions = HomeFragmentDirections.actionHomeFragmentToAddPaymentDialog();
            navController.navigate(directions);
        });

        mainViewmodel.getLedger().observe(getViewLifecycleOwner(), ledger -> {
                    if (ledger != null) {
                        updateTotalAmount(ledger.getRemainingAmount());
                        updateAddPaymentButtonVisibility(ledger.getRemainingAmount());
                        updatePaymentChips(ledger);
                    }
                });

        mainViewmodel.getPaymentList().observe(getViewLifecycleOwner(), payments -> {
            for (Payment p : payments.values()) {
                if (!mainViewmodel.checkIfPaymentModeExistInLedger(p.getType())) {
                    String text = p.getType() + " = " + "Rs " + p.getAmount();
                    addInputChip(text, p.getType(), binding.paymentsChipGroup);
                }
            }
        });
    }

    private void updateTotalAmount(double amount) {
        binding.totalAmount.setText("Total Amount = " + amount);
    }

    private void updateAddPaymentButtonVisibility(double remainingAmount) {
        binding.addPaymentButton.setVisibility(remainingAmount == 0.0 ? View.GONE : View.VISIBLE);
    }

    private void updatePaymentChips(Ledger ledger) {
        binding.paymentsChipGroup.removeAllViews();

        for (Payment p : ledger.getPayments()) {
            String text = p.getType() + " = " + "Rs " + p.getAmount();
            addInputChip(text, p.getType(), binding.paymentsChipGroup);
        }
    }


    private void addInputChip(String text, PaymentMode mode, ChipGroup paymentsChipGroup) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setTag(mode);
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setFocusable(false);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setChipStrokeColorResource(R.color.black);
        chip.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge);
        chip.setChipStartPadding(20f);
        chip.setChipEndPadding(20f);

        chip.setChipDrawable(ChipDrawable.createFromAttributes(
                requireContext(), null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry));


        chip.setOnCloseIconClickListener(view -> {
            PaymentMode modeToRemove = (PaymentMode) chip.getTag();
            if (modeToRemove != null) {
                mainViewmodel.removePayment(modeToRemove);
            }
            paymentsChipGroup.removeView(chip);
        });

        paymentsChipGroup.addView(chip);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}