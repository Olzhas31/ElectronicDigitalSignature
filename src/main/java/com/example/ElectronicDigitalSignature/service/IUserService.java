package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    void create(String iin, String name, String surname, String midName, String email, String gender, String phoneNumber);

    UserEntity getByEmail(String email);

    void update(Long userId, String name, String surname, String midName, String password);

    List<UserEntity> getManagers();
}
