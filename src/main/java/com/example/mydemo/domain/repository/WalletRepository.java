package com.example.mydemo.domain.repository;

import java.util.List;

import com.example.mydemo.domain.entity.WalletEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, String> {
    public List<WalletEntity> findByUsername(String username);
}
