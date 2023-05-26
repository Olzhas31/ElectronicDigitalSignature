package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.OtinishEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;

import java.util.List;

public interface IOtinishService {
    void create(UserEntity user, String type, String organ, String description, String address);

    List<OtinishEntity> getByUser(UserEntity user);

    OtinishEntity getById(Long id);

    List<OtinishEntity> getAll();
}
