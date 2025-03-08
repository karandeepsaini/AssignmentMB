package com.example.merabillassignment.enums;

public enum PaymentMode {
    CASH("CASH"),
    CREDIT("CREDIT CARD"),
    BANK("BANK TRANSFER");

    private final String value;

    PaymentMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMode fromString(String value) {
        for (PaymentMode mode : PaymentMode.values()) {
            if (mode.getValue().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid payment type: " + value);
    }
}
