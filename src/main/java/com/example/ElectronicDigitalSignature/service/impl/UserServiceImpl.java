package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.entity.KeyEntity;
import com.example.ElectronicDigitalSignature.entity.Roles;
import com.example.ElectronicDigitalSignature.entity.TokenEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.exception.AppException;
import com.example.ElectronicDigitalSignature.exception.EmailAlreadyExistsException;
import com.example.ElectronicDigitalSignature.exception.IinAlreadyExistsException;
import com.example.ElectronicDigitalSignature.repository.UserRepository;
import com.example.ElectronicDigitalSignature.service.IEmailService;
import com.example.ElectronicDigitalSignature.service.IKeyService;
import com.example.ElectronicDigitalSignature.service.ITokenService;
import com.example.ElectronicDigitalSignature.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IKeyService keyService;
    private final IEmailService emailService;
    private final ITokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String iin) throws UsernameNotFoundException {
        var user = userRepository.findByIin(iin)
                .orElseThrow(() -> new UsernameNotFoundException("User with iin " + iin + " not found"));
//        if (user.getLocked()) {
//            throw new RuntimeException("blocked");
//        }
//        if (!user.getEnabled()) {
//            throw new RuntimeException("notEnabled");
//        }
        return user;
    }

    @Override
    public void create(String iin, String name, String surname, String midName, String email) {
        if (userRepository.existsByIin(iin)) {
            throw new IinAlreadyExistsException("iin is already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("email is already exists");
        }

        String password = generateRandomPassword(16);
        UserEntity user = UserEntity.builder()
                .iin(iin)
                .midName(midName)
                .name(name)
                .surname(surname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Roles.USER.toString())
                .build();
        user = userRepository.save(user);

        KeyEntity keyEntity = keyService.createKey(user);
        String code = tokenService.create(user);
        emailService.sendEmail(password, email, code);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public void update(Long id, String name, String surname, String midName, String password) {
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        user.setName(name);
        user.setSurname(surname);
        user.setMidName(midName);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        System.out.println("password: " + sb);
        return sb.toString();
    }
}
