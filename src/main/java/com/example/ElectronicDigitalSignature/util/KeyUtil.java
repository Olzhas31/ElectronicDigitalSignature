package com.example.ElectronicDigitalSignature.util;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

@Component
public class KeyUtil {

    public KeyPair generateRsaKeys() {
        KeyPairGenerator pairGenerator = null;
        try {
            pairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            pairGenerator.initialize(1024, random);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        return pairGenerator.generateKeyPair();
    }

    public void saveKey(final String filePath, String file, final Object key) throws FileNotFoundException, IOException {
        if (key != null){
            Path uploadPath = Paths.get(filePath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            FileOutputStream fos = new FileOutputStream(filePath + "/" + file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(key);
            oos.close();
            fos.close();
        }
    }
}
