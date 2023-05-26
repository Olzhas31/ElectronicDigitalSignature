package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.entity.OtinishEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.exception.AppException;
import com.example.ElectronicDigitalSignature.repository.OtinishRepository;
import com.example.ElectronicDigitalSignature.service.IOtinishService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OtinishServiceImpl implements IOtinishService {

    private final OtinishRepository repository;

    @Override
    public void create(UserEntity user, String type, String organ, String description, String address) {
        OtinishEntity otinish = OtinishEntity.builder()
                .type(type)
                .organ(organ)
                .description(description)
                .address(address)
                .createdTime(LocalDateTime.now())
                .owner(user)
                .build();
        repository.save(otinish);
    }

    @Override
    public List<OtinishEntity> getByUser(UserEntity user) {
        return repository.findAllByOwner(user);
    }

    @Override
    public OtinishEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException("Otinish not found"));
    }

    @Override
    public List<OtinishEntity> getAll() {
        return repository.findAll();
    }
}
