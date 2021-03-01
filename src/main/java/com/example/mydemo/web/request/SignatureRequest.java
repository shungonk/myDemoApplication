package com.example.mydemo.web.request;

import java.security.GeneralSecurityException;

import com.example.mydemo.util.SecurityUtil;
import com.example.mydemo.util.StringUtil;
import com.google.gson.GsonBuilder;

public abstract class SignatureRequest {

    protected String publicKey; // will be set in signate method
    protected String signature; // will be set in signate method

    protected SignatureRequest() {}

    public String getPublicKey() {
        return publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public abstract byte[] getData();

    public abstract boolean validateFields();

    public final void signate(String privateKey, String publicKey) {
        try {
            this.publicKey = publicKey;
            this.signature = SecurityUtil.createEcdsaSign(privateKey, getData());
        } catch (GeneralSecurityException e) {
            // LogWriter.warning("Failed to create ECDSA signature");
        }
    }

    public final boolean verifySignature() {
        try {
            return SecurityUtil.verifyEcdsaSign(publicKey, getData(), signature);
        } catch (GeneralSecurityException e) {
            // LogWriter.warning("Failed to verity signature");
            return false;
        }
    }

    public String toJson() {
        return StringUtil.toJson(this);
    }

    public String toJsonPrettyPrinting() {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        return gsonBuilder.toJson(this);
    }
}