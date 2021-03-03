package com.example.mydemo.domain.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wallet")
public class WalletEntity implements Serializable {

    private static final long serialVersionUID = 51217068822683539L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "blockchain_address", nullable = false, length = 100)
    private String address;

    @Column(name = "private_key", nullable = false, length = 200)
    private String privateKey;

    @Column(name = "public_key", nullable = false, length = 200)
    private String publicKey;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

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

    public UserEntity getUser() {
        return user;
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

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
