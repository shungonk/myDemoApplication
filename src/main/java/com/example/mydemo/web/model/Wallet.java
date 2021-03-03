package com.example.mydemo.web.model;

import com.example.mydemo.domain.entity.WalletEntity;
import com.example.mydemo.util.SecurityUtil;

public class Wallet {
    
    private String name;
    private String address;
    private String privateKey;
    private String publicKey;
    private String balanceStr;

    public Wallet(String name, String address, String privateKey, String publicKey) {
        this.name = name;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public static Wallet fromEntity(WalletEntity e) {
        if (e == null) return null;
        return new Wallet(e.getName(), e.getAddress(), e.getPrivateKey(), e.getPublicKey());
    }

    public WalletEntity toEntity(String username) {
        return new WalletEntity(name, username, address, privateKey, publicKey);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getBalanceStr() {
        return balanceStr;
    }

    public void setBalanceStr(String balanceStr) {
        this.balanceStr = balanceStr;
    }

    public static Wallet create(String name) {
        try {
            var keyPair = SecurityUtil.generateKeyPair();            
            var pvt = keyPair.getPrivate();
            var pub = keyPair.getPublic();

            return new Wallet(
                name,
                SecurityUtil.getAddressFromPublicKey(pub), 
                SecurityUtil.encodeKeyToString(pvt),
                SecurityUtil.encodeKeyToString(pub));
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
