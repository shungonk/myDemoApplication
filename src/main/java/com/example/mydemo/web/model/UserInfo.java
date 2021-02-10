package com.example.mydemo.web.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserInfo extends User {

    private static final long serialVersionUID = 2584805843445698023L;
    
    private final Map<String, Wallet> wallets;

    public UserInfo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Map<String, Wallet> wallets) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.wallets = wallets;
    }

    public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Wallet> wallets) {
        super(username, password, authorities);
        this.wallets = wallets;
    }

    public boolean hasWalletNamed(String name) {
        return wallets.containsKey(name);
    }

    public boolean hasWallet(Wallet wallet) {
        return wallets.containsValue(wallet);
    }

    public Optional<Wallet> getWallet(String name) {
        return Optional.ofNullable(wallets.get(name));
    }

    public Map<String, Wallet> getWallets() {
        return Collections.unmodifiableMap(wallets);
    }

    //TODO: have to access to wallet table in the below methods.

    public void addWallet(String name, Wallet wallet) {
        wallets.putIfAbsent(name, wallet);
    }

    public void addNewWallet(String name) {
        Wallet newWallet = Wallet.generate();
        wallets.putIfAbsent(name, newWallet);
    }

    public void removeWallet(String name) {
        wallets.remove(name);
    }
}
