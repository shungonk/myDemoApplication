package com.example.mydemo.web.model;

import com.example.mydemo.domain.entity.WalletEntity;

public final class Wallet {
    
    private final String blockchainAddress;
    private final String publicKey;
    private final String privateKey;

    private Wallet(String blockchainAddress, String publicKey, String privateKey) {
        this.blockchainAddress = blockchainAddress;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static Wallet of(String blockchainAddress, String publicKey, String privateKey) {
        return new Wallet(blockchainAddress, publicKey, privateKey);
    }

    public static Wallet ofEntity(WalletEntity entity) {
        return Wallet.of(entity.getBlockchainAddress(), entity.getPublicKey(), entity.getPrivateKey());
    }

    public static Wallet generate() {
        //TODO: write codes of generating privatekey, publickey, and blockchianaddress
        return null;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blockchainAddress == null) ? 0 : blockchainAddress.hashCode());
        result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
        result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Wallet other = (Wallet) obj;
        if (blockchainAddress == null) {
            if (other.blockchainAddress != null)
                return false;
        } else if (!blockchainAddress.equals(other.blockchainAddress))
            return false;
        if (privateKey == null) {
            if (other.privateKey != null)
                return false;
        } else if (!privateKey.equals(other.privateKey))
            return false;
        if (publicKey == null) {
            if (other.publicKey != null)
                return false;
        } else if (!publicKey.equals(other.publicKey))
            return false;
        return true;
    }

}
