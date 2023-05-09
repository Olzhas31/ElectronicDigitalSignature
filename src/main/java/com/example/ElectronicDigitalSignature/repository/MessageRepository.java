package com.example.ElectronicDigitalSignature.repository;

import com.example.ElectronicDigitalSignature.entity.MessageEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByReceiver(UserEntity user);

    List<MessageEntity> findAllBySender(UserEntity user);
}
