package com.example.mydemo.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wallet")
public class WalletEntity implements Serializable {

    private static final long serialVersionUID = 51217068822683539L;

    @Id
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="username")
    private String username;

    @Column(name="blockchain_address")
    private String address;

    @Column(name="private_key")
    private String privateKey;

    @Column(name="public_key")
    private String publicKey;

    public WalletEntity() {}

    public WalletEntity(String name, String username, String address, String privateKey, String publicKey) {
        this.name = name;
        this.username = username;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
