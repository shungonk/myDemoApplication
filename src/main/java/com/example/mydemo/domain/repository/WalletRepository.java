package com.example.mydemo.domain.repository;

import java.util.List;

import com.example.mydemo.domain.entity.WalletEntity;
import com.example.mydemo.domain.entity.WalletPk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, WalletPk> {
    public List<WalletEntity> findByUsername(String username);
    public WalletEntity findByNameAndUsername(String name, String username);
}
