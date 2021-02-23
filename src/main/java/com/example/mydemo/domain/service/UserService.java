package com.example.mydemo.domain.service;

import com.example.mydemo.domain.entity.UserEntity;
import com.example.mydemo.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null || !userEntity.isEnabled()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        return new User(userEntity.getUsername(), userEntity.getPassword(), AuthorityUtils.NO_AUTHORITIES);
    }

    @Transactional
    public void registerUser(String username, String password) {
        userRepository.save(new UserEntity(username, passwordEncoder.encode(password), true));
    }

    public boolean isUsernameExists(String username) {
        return userRepository.countByUsername(username) != 0;
    }
}
