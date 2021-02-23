package com.example.mydemo.domain.repository;

import com.example.mydemo.domain.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    public UserEntity findByUsername(String username);
    public long countByUsername(String username);
}
