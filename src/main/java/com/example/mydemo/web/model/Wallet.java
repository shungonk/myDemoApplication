package com.example.mydemo.web.model;

import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

import com.example.mydemo.domain.entity.WalletEntity;
import com.example.mydemo.util.SecurityUtil;

public class Wallet {
    
    private String name;
    private String address;
    private String privateKey;
    private String publicKey;
    private boolean mine;
    private float balance;

    public Wallet(String name, String address, String privateKey, String publicKey, boolean mine) {
        this.name = name;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.mine = mine;
    }

    public static Wallet fromEntity(WalletEntity e) {
        if (e == null) return null;
        return new Wallet(e.getName(), e.getAddress(), e.getPrivateKey(), e.getPublicKey(), e.isMine());
    }

    public WalletEntity toEntity(String username) {
        return new WalletEntity(name, username, address, privateKey, publicKey, mine);
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

    public boolean isMine() {
        return mine;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public static Wallet create(String name) {
        try {
            // create the KeyPair, from which you can obtain the public and private keys.
            //TODO: what the difference between getInstance("EC") and getInstance("ECDSA", "BC")?
            var keyGenerator = KeyPairGenerator.getInstance("EC");
            var ecGenSpec = new ECGenParameterSpec("secp256k1");
            keyGenerator.initialize(ecGenSpec);
            var keyPair = keyGenerator.genKeyPair();
            var pvt = keyPair.getPrivate();
            var pub = keyPair.getPublic();

            return new Wallet(
                name,
                SecurityUtil.getAddressFromPublicKey(pub), 
                SecurityUtil.encodeKeyToString(pvt),
                SecurityUtil.encodeKeyToString(pub),
                false);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
