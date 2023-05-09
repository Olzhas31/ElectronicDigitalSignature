package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.entity.KeyEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.repository.KeyRepository;
import com.example.ElectronicDigitalSignature.service.IKeyService;
import com.example.ElectronicDigitalSignature.util.KeyUtil;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class KeyServiceImpl implements IKeyService {

    private final KeyRepository keyRepository;
    private final KeyUtil keyUtil;
    private final String dir = "keys/";

    @Override
    public KeyEntity createKey(UserEntity user) {
        KeyPair keyPair = keyUtil.generateRsaKeys();
        try {
            keyUtil.saveKey(dir + user.getId(), "private.key", keyPair.getPrivate());
            keyUtil.saveKey(dir + user.getId(), "public.key", keyPair.getPublic());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        KeyEntity key = KeyEntity.builder()
                .expireDate(LocalDate.now().plusYears(1))
                .publicKey(Base64.encodeBase64String(keyPair.getPublic().getEncoded()))
                .privateKey(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()))
                .user(user)
                .build();
        return keyRepository.save(key);
    }

    @Override
    public KeyEntity getByUser(UserEntity user) {
        KeyEntity keyEntity = keyRepository.findByUser(user)
                .orElse(null);
        return keyEntity;
    }

    @Override
    public void test(UserEntity user) {
        try {
            FileInputStream inputStream = new FileInputStream("keys/17/private.key");
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            PrivateKey privateKey = (PrivateKey) ois.readObject();
            Signature signature = Signature.getInstance(privateKey.getAlgorithm());
            SignedObject signedObject = new SignedObject("new String(file.getBytes())", privateKey, signature);

            FileOutputStream fs = new FileOutputStream("signs/" + "1.sign");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(signedObject);
            boolean result = verify();
            System.out.println("res: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean verify() {
        System.out.println("verify");
        try {
            FileInputStream fis2 = new FileInputStream("keys/17/public.key");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            PublicKey publicKey = null;
            try {
                publicKey = (PublicKey) ois2.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Signature signature2 = null;
            try {
                signature2 = Signature.getInstance(publicKey.getAlgorithm());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            SignedObject signedObject = null;
            try (FileInputStream fis = new FileInputStream("signs/" + "1.sign");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                signedObject = (SignedObject) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }

            boolean verified = false;
            try {
                verified = signedObject.verify(publicKey, signature2);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            }

            return verified;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
