package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    void create(String iin, String name, String surname, String midName, String email);

    UserEntity getByEmail(String email);

    void update(Long userId, String name, String surname, String midName, String password);
}
