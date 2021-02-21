package com.example.mydemo.domain.repository;

import java.util.List;

import com.example.mydemo.domain.entity.WalletEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, Integer> {
    public WalletEntity findByNameAndUsername(String name, String username);
    public long countByNameAndUsername(String name, String username);
    public List<WalletEntity> findByUsernameOrderById(String username);
    public long countByUsername(String username);
}
