package com.example.mydemo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mydemo.domain.entity.UserEntity;
import com.example.mydemo.domain.entity.WalletEntity;
import com.example.mydemo.domain.repository.UserRepository;
import com.example.mydemo.domain.repository.WalletRepository;
import com.example.mydemo.web.model.UserAccount;
import com.example.mydemo.web.model.Wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        UserEntity user = userRepository.findByUsername(username);

        if (user == null || user.isEnabled()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        //TODO: consider authority
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;

        Map<String, Wallet> wallets = walletRepository.findByUsername(user.getUsername())
                                        .stream()
                                        .collect(Collectors.toMap(WalletEntity::getName, Wallet::ofEntity));

        //TODO: check password by encoding
        

        UserAccount userAccount = new UserAccount(user.getUsername(), user.getPassword(), authorities, wallets);

        return userAccount;
    }
    
}
