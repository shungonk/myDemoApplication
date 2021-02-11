package com.example.mydemo.web.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mydemo.domain.repository.WalletRepository;
import com.example.mydemo.web.model.Wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    public Wallet findByPrimaryKey(String name, String username) {
        return Wallet.fromEntity(walletRepository.findByNameAndUsername(name, username));
    }

    public List<Wallet> findByUsername(String username) {
        return walletRepository.findByUsername(username)
                    .stream()
                    .map(Wallet::fromEntity)
                    .sorted((w1, w2) -> w1.getName().compareTo(w2.getName()))
                    .collect(Collectors.toList());
    }
}
