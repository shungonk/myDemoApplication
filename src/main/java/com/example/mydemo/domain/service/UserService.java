package com.example.mydemo.domain.service;

import com.example.mydemo.domain.entity.UserEntity;
import com.example.mydemo.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(String username, String password) {
        userRepository.save(new UserEntity(username, passwordEncoder.encode(password), true, true));
    }

    @Transactional
    public void registerInactiveUser(String username, String password) {
        userRepository.save(new UserEntity(username, passwordEncoder.encode(password), true, false));
    }

    public boolean isUsernameExists(String username) {
        return userRepository.countByUsername(username) != 0;
    }

    public boolean isUserActive(String username) {
        return userRepository.findByUsername(username).isActive();
    }
}
