package com.example.mydemo.domain.service;

import com.example.mydemo.domain.entity.UserEntity;
import com.example.mydemo.domain.repository.UserRepository;
import com.example.mydemo.domain.repository.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
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

        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null || userEntity.isEnabled()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        return new User(userEntity.getUsername(), userEntity.getPassword(), AuthorityUtils.NO_AUTHORITIES);
    }
}
