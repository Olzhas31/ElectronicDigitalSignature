package com.example.ElectronicDigitalSignature.service;

import com.example.ElectronicDigitalSignature.entity.MessageEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMessageService {

    List<MessageEntity> getAllByReceiver(UserEntity user);

    List<MessageEntity> getAllBySender(UserEntity user);

    Long send(UserEntity sender, String email, String title, String content, MultipartFile key);

    MessageEntity getById(Long id, UserEntity user);

    boolean isSign(Long messageId, Long senderId);
}
