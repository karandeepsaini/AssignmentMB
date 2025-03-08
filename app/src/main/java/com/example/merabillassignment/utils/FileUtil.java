package com.example.merabillassignment.utils;

import android.content.Context;
import java.io.*;

import android.content.Context;

import com.example.merabillassignment.models.Ledger;
import com.example.merabillassignment.models.Payment;
import com.example.merabillassignment.models.PaymentTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;

public class FileUtil {
    private static final String FILE_NAME = "file.txt";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Payment.class, new PaymentTypeAdapter())
            .create();


    public static void saveLedger(Context context, Ledger ledger) {
        String jsonData = gson.toJson(ledger);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Ledger loadLedger(Context context) {
        StringBuilder jsonData = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader reader = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }

            return gson.fromJson(jsonData.toString(), Ledger.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Ledger();
    }
}

