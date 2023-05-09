package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.entity.MessageEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.exception.AppException;
import com.example.ElectronicDigitalSignature.exception.ReceiverEmailInSystemNotFoundException;
import com.example.ElectronicDigitalSignature.repository.MessageRepository;
import com.example.ElectronicDigitalSignature.repository.UserRepository;
import com.example.ElectronicDigitalSignature.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public List<MessageEntity> getAllByReceiver(UserEntity user) {
        return messageRepository.findAllByReceiver(user).stream().sorted().toList();
    }

    @Override
    public List<MessageEntity> getAllBySender(UserEntity user) {
        return messageRepository.findAllBySender(user).stream().sorted().toList();
    }

    @Override
    public Long send(UserEntity sender, String email, String title, String content, MultipartFile key) {
        UserEntity receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new ReceiverEmailInSystemNotFoundException("email not found"));
        MessageEntity message = MessageEntity.builder()
                .title(title)
                .sendTime(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .content(content)
                .build();
        message = messageRepository.save(message);
        if (key.getSize() > 0) {
            try {
                ObjectInputStream ois = new ObjectInputStream(key.getInputStream());
                PrivateKey privateKey = (PrivateKey) ois.readObject();
                Signature signature = Signature.getInstance(privateKey.getAlgorithm());
                SignedObject signedObject = new SignedObject(content, privateKey, signature);

                FileOutputStream fs = new FileOutputStream("signs/" + message.getId() + ".sign");
                ObjectOutputStream os = new ObjectOutputStream(fs);
                os.writeObject(signedObject);
            } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return message.getId();
    }

    @Override
    public MessageEntity getById(Long id, UserEntity user) {
        MessageEntity message = messageRepository.findById(id)
                .orElseThrow(() -> new AppException("message by id " + id + " not found"));
        if (message.getSender().equals(user) || (message.getReceiver() != null && message.getReceiver().equals(user))) {
            if (message.getReceiver() != null && message.getReceiver().equals(user)) {
                message.setIsRead(true);
                messageRepository.save(message);
            }
            return message;
        }
        throw new AppException("Доступ запрещен");
    }

    @Override
    public boolean isSign(Long messageId, Long senderId) {
        try {
            FileInputStream fis2 = new FileInputStream("keys/" + senderId  + "/public.key");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            PublicKey publicKey = (PublicKey) ois2.readObject();

            Signature signature2 = Signature.getInstance(publicKey.getAlgorithm());

            SignedObject signedObject = null;
            try (FileInputStream fis = new FileInputStream("signs/" + messageId + ".sign");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                signedObject = (SignedObject) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }

            boolean verified = false;
            try {
                verified = signedObject.verify(publicKey, signature2);
            } catch (InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }

            return verified;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
