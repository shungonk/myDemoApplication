package com.example.mydemo.web.request;

import java.math.BigDecimal;

import com.example.mydemo.util.SecurityUtil;
import com.example.mydemo.util.StringUtil;
import com.google.gson.GsonBuilder;

public class TransactionRequest {

    private String senderPublicKey;
    private String senderAddress;
    private String recipientAddress;
    private BigDecimal value;
    private String signature;
    private long timestamp;

    public TransactionRequest(String senderPublicKey, String senderAddress, String recipientAddress, BigDecimal value, long timestamp, String signature) {
        this.senderPublicKey = senderPublicKey;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.value = value;
        this.timestamp = timestamp;
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

    public BigDecimal getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean validateTransactionRequest() {
        if (senderPublicKey == null || senderPublicKey.isBlank() ||
            senderAddress == null || senderAddress.isBlank() ||
            recipientAddress == null || recipientAddress.isBlank() ||
            value == null || value.equals(BigDecimal.ZERO) ||
            signature == null || signature.isBlank() ||
            timestamp == 0) {
            return false;
        }
        return true;
    }

    public boolean validateValue() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean verifySignature() {
        return SecurityUtil.verifyEcdsaSign(
            senderPublicKey,
            senderAddress + recipientAddress + value.toPlainString() + Long.toString(timestamp),
            signature
            );
    }

    public boolean veritfyAddress() {
        return SecurityUtil.validateAddressByPublicKey(senderAddress, senderPublicKey);
    }

    public String marshalJson() {
        return StringUtil.toJson(this);
    }

    public String marshalJsonPrettyPrinting() {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        return gsonBuilder.toJson(this);
    }

    public static TransactionRequest fromJson(String json) {
        return StringUtil.fromJson(json, TransactionRequest.class);
    }
    
}
