package com.example.mydemo.web.model;

import com.example.mydemo.domain.entity.WalletEntity;

public class Wallet {
    
    private String name;
    private String blockchainAddress;
    private String publicKey;
    private String privateKey;

    public Wallet(String name, String blockchainAddress, String publicKey, String privateKey) {
        this.name = name;
        this.blockchainAddress = blockchainAddress;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static Wallet ofEntity(WalletEntity entity) {
        if (entity == null) return null;
        return new Wallet(entity.getName(), entity.getBlockchainAddress(), entity.getPublicKey(), entity.getPrivateKey());
    }

    public static Wallet generate() {
        //TODO: write codes of generating privatekey, publickey, and blockchianaddress
        return null;
    }

    public String getName() {
        return name;
    }

    public String getBlockchainAddress() {
        return blockchainAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
