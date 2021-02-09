package com.example.mydemo.web.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAccount extends User {

    private static final long serialVersionUID = 2584805843445698023L;
    
    private final Map<String, Wallet> wallets;

    public UserAccount(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Map<String, Wallet> wallets) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.wallets = wallets;
    }

    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Wallet> wallets) {
        super(username, password, authorities);
        this.wallets = wallets;
    }

    public boolean addWallet(String name, Wallet wallet) {
        return wallets.putIfAbsent(name, wallet) == null;
    }

    public boolean addNewWallet(String name) {
        return wallets.putIfAbsent(name, Wallet.generate()) == null;
    }

    public Optional<Map.Entry<String, Wallet>> getWallet(String name) {
        return wallets.entrySet().stream().filter(e -> e.getKey().equals(name)).findAny();
    }

    public boolean removeWallet(Map.Entry<String, Wallet> entry) {
        return wallets.remove(entry.getKey(), entry.getValue());
    }

    public Map<String, Wallet> getUnmodifiedWallets() {
        return Collections.unmodifiableMap(wallets);
    }
}
