package com.example.mydemo.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class UserEntity {

    @Id
    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="active")
    private boolean active;

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
}
