package com.example.mydemo.web.request;

import com.example.mydemo.util.SecurityUtil;
import com.example.mydemo.util.StringUtil;
import com.google.gson.GsonBuilder;

public class PurchaseRequest {

    private String publicKey;
    private String address;
    private float value;
    private String signature;

    public PurchaseRequest(String publicKey, String address, float value, String signature) {
        this.publicKey = publicKey;
        this.address = address;
        this.value = value;
        this.signature = signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAddress() {
        return address;
    }

    public float getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public boolean validateTransactionRequest() {
        if (publicKey == null || publicKey.isBlank() ||
            address == null || address.isBlank() ||
            Float.compare(value, 0f) <= 0 ||
            signature == null || signature.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean verifySignature() {
        return SecurityUtil.verifyEcdsaSign(
            publicKey,
            address + Float.toString(value),
            signature
            );
    }

    public String marshalJson() {
        return StringUtil.toJson(this);
    }

    public String marshalJsonPrettyPrinting() {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        return gsonBuilder.toJson(this);
    }

    public static PurchaseRequest fromJson(String json) {
        return StringUtil.fromJson(json, PurchaseRequest.class);
    }
    
}
