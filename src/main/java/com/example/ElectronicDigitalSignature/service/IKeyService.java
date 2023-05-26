package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.KeyEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IKeyService {
    KeyEntity createKey(UserEntity user);

    KeyEntity getByUser(UserEntity user);

    void test(UserEntity user);

    boolean verifyKey(UserEntity user, MultipartFile key);
}
