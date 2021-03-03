package com.example.mydemo.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<WalletEntity> wallets;

    public UserEntity() {}

    public UserEntity(String username, String password, boolean enabled, boolean active) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isActive() {
        return active;
    }

    public List<WalletEntity> getWallets() {
        return wallets;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setWallets(List<WalletEntity> wallets) {
        this.wallets = wallets;
    }
}
