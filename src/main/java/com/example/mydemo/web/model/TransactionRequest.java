package com.example.mydemo.web.model;

import java.io.Serializable;

import com.example.mydemo.util.SecurityUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TransactionRequest implements Serializable {

	private static final long serialVersionUID = 1L;

    private String senderPublicKey;
    private String senderAddress;
    private String recipientAddress;
    private float value;
    private String signature;

    public TransactionRequest(String senderPublicKey, String senderAddress, String recipientAddress, float value, String signature) {
        this.senderPublicKey = senderPublicKey;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.value = value;
        this.signature = signature;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public float getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public String calculateHash() {
        return SecurityUtil.sha256(senderAddress + recipientAddress + Float.toString(value));
    }

    public boolean validateTransactionRequest() {
        if (senderPublicKey == null || senderPublicKey.isBlank() ||
            senderAddress == null || senderAddress.isBlank() ||
            recipientAddress == null || recipientAddress.isBlank() ||
            Float.compare(value, 0f) <= 0 ||
            signature == null || signature.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean verifySignature() {
        return SecurityUtil.verifyEcdsaSign(
            senderPublicKey,
            senderAddress + recipientAddress + Float.toString(value),
            signature
            );
    }

    public String marshalJson() {
        return new Gson().toJson(this);
    }

    public String marshalJsonPrettyPrinting() {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        return gsonBuilder.toJson(this);
    }

    public static TransactionRequest fromJson(String json) {
        return new Gson().fromJson(json, TransactionRequest.class);
    }
    
}
