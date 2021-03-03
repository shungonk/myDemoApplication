package com.example.mydemo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mydemo.domain.repository.WalletRepository;
import com.example.mydemo.web.model.Wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    public Wallet findByNameAndUsername(String name, String username) {
        return Wallet.fromEntity(walletRepository.findByNameAndUsername(name, username));
    }

    public long countByNameAndUsername(String name, String username) {
        return walletRepository.countByNameAndUsername(name, username);
    }

    public List<Wallet> findByUsername(String username) {
        return walletRepository.findByUsernameOrderById(username)
                    .stream()
                    .map(Wallet::fromEntity)
                    .collect(Collectors.toList());
    }

    public long countByUsername(String username) {
        return walletRepository.countByUsername(username);
    }

    @Transactional
    public void save(String username, Wallet wallet) {
        walletRepository.save(wallet.toEntity(username));
    }
}
