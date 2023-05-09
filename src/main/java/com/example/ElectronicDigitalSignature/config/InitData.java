package com.example.ElectronicDigitalSignature.config;

import com.example.ElectronicDigitalSignature.entity.Roles;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // {iin, name, surname, midName, email}
    private final String[][] users = {
            {"1", "Олжас", "Сулейменов", "Телжанович", "suleimenov97@gmail.com"},
            {"2", "Аман", "Тоғжан", "Аманқызы", "togzhan@mail.ru"}
    };


    @Override
    public void run(String... args) {
        initAdmin();
        initUsers();
    }

    private void initUsers() {
        for (String[] strings : users) {
            UserEntity user = UserEntity.builder()
                    .iin(strings[0])
                    .password(passwordEncoder.encode("1"))
                    .role(Roles.USER.toString())
                    .name(strings[1])
                    .surname(strings[2])
                    .midName(strings[3])
                    .email(strings[4])
                    .build();
            if (!userRepository.existsByIin(user.getIin())) {
                userRepository.save(user);
            }
        }
    }

    private void initAdmin() {
        UserEntity admin = UserEntity.builder()
                .iin("admin")
                .password(passwordEncoder.encode("100"))
                .role(Roles.ADMIN.toString())
                .build();
        if (!userRepository.existsByIin(admin.getIin())) {
            userRepository.save(admin);
        }
    }
}
