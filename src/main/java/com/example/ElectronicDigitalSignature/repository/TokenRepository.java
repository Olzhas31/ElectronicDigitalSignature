package com.example.ElectronicDigitalSignature.repository;

import com.example.ElectronicDigitalSignature.entity.TokenEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByCode(String code);

    List<TokenEntity> findAllByUser(UserEntity user);
}
