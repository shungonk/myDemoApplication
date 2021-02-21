package com.example.mydemo.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="wallet")
@IdClass(WalletPk.class)
public class WalletEntity implements Serializable {

    private static final long serialVersionUID = 51217068822683539L;

    @Id
    @Column(name="name")
    private String name;

    @Id
    @Column(name="username")
    private String username;

    @Column(name="blockchain_address")
    private String address;

    @Column(name="public_key")
    private String publicKey;

    @Column(name="private_key")
    private String privateKey;

    public WalletEntity() {}

    public WalletEntity(String name, String username, String address, String publicKey, String privateKey) {
        this.name = name;
        this.username = username;
        this.address = address;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
