package com.example.mydemo.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wallet")
public class WalletEntity {

    @Id
    @Column(name="name")
    private String name;

    @Id
    @Column(name="username")
    private String username;

    @Column(name="blockchain_address")
    private String blockchainAddress;

    @Column(name="public_key")
    private String publicKey;

    @Column(name="private_key")
    private String privateKey;

    public WalletEntity() {}

    public WalletEntity(String name, String username, String blockchainAddress, String publicKey, String privateKey) {
        this.name = name;
        this.username = username;
        this.blockchainAddress = blockchainAddress;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBlockchainAddress(String blockchainAddress) {
        this.blockchainAddress = blockchainAddress;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
