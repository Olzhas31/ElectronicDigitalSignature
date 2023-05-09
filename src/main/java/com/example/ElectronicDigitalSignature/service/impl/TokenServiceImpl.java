package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.entity.TokenEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.repository.TokenRepository;
import com.example.ElectronicDigitalSignature.service.ITokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final TokenRepository tokenRepository;

    @Override
    public String create(UserEntity user) {
        return tokenRepository.save(
                TokenEntity.builder()
                        .createdTime(LocalDateTime.now())
                        .code("6254")
                        .user(user)
                        .build()
        ).getCode();
    }

    @Override
    public UserEntity getUserByCode(String code) {
        return tokenRepository.findByCode(code)
                .map(TokenEntity::getUser)
                .orElse(null);
    }

    @Override
    public boolean existsTokenByEmail(UserEntity user, String code) {
        List<TokenEntity> tokens = tokenRepository.findAllByUser(user);
        if (tokens.size() == 0) {
            return false;
        }
        for (TokenEntity token : tokens) {
            if (token.getCreatedTime().isAfter(LocalDateTime.now().minusMinutes(5))) {
                if (token.getCode().equals(code)) {
                    return true;
                }
            }
        }
        return false;
    }
}
