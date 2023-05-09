package com.example.ElectronicDigitalSignature.repository;

import com.example.ElectronicDigitalSignature.entity.KeyEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyRepository extends JpaRepository<KeyEntity, Long> {

    Optional<KeyEntity> findByUser(UserEntity user);
}
