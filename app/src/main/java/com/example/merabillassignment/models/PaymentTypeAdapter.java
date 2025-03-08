package com.example.merabillassignment.models;

import com.google.gson.*;
import java.lang.reflect.Type;

public class PaymentTypeAdapter implements JsonDeserializer<Payment>, JsonSerializer<Payment> {
    @Override
    public Payment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "CASH":
                return context.deserialize(json, CashPayment.class);
            case "BANK":
                return context.deserialize(json, BankPayment.class);
            case "CREDIT":
                return context.deserialize(json, CreditCardPayment.class);
            default:
                throw new JsonParseException("Unknown payment type: " + type);
        }
    }

    @Override
    public JsonElement serialize(Payment src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
