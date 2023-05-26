package com.example.ElectronicDigitalSignature.repository;

import com.example.ElectronicDigitalSignature.entity.OtinishEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtinishRepository extends JpaRepository<OtinishEntity, Long> {

    List<OtinishEntity> findAllByOwner(UserEntity user);
}
