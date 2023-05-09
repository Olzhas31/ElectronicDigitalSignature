package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.UserEntity;

public interface ITokenService {
    String create(UserEntity user);

    UserEntity getUserByCode(String code);

    boolean existsTokenByEmail(UserEntity user, String code);
}
