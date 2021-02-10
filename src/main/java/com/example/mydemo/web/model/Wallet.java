package com.example.mydemo.web.model;

import com.example.mydemo.domain.entity.WalletEntity;

public final class Wallet {
    
    private final String name;
    private final String blockchainAddress;
    private final String publicKey;
    private final String privateKey;

    private Wallet(String name, String blockchainAddress, String publicKey, String privateKey) {
        this.name = name;
        this.blockchainAddress = blockchainAddress;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static Wallet of(String name, String blockchainAddress, String publicKey, String privateKey) {
        return new Wallet(name, blockchainAddress, publicKey, privateKey);
    }

    public static Wallet ofEntity(WalletEntity entity) {
        return Wallet.of(entity.getName(), entity.getBlockchainAddress(), entity.getPublicKey(), entity.getPrivateKey());
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
