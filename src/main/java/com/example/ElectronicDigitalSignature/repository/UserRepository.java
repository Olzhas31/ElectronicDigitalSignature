package com.example.ElectronicDigitalSignature.repository;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByIin(String iin);

    boolean existsByIin(String iin);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
