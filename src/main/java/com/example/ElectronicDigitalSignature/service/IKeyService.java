package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.KeyEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;

public interface IKeyService {
    KeyEntity createKey(UserEntity user);

    KeyEntity getByUser(UserEntity user);

    void test(UserEntity user);
}
