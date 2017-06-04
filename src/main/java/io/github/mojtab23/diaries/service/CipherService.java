package io.github.mojtab23.diaries.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by mojtab23 on 6/3/2017.
 */

@Service
public class CipherService {


    private SecretKeyFactory factory;
    private Cipher cipher;

    @PostConstruct
    private void init() {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }


    public byte[] encrypt(char[] key, byte[] value, byte[] salt, byte[] IvBytes) {
        try {

            final SecretKeySpec secret = createSecretKey(key, salt);
            final IvParameterSpec iv = createIv(IvBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
            return cipher.doFinal(value);


        } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    public byte[] decrypt(char[] key, byte[] value, byte[] salt, byte[] ivBytes) {


        try {
            final SecretKeySpec secret = createSecretKey(key, salt);
            final IvParameterSpec iv = createIv(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secret, iv);
            return cipher.doFinal(value);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private SecretKeySpec createSecretKey(char[] key, byte[] salt) {
        try {

            KeySpec spec = new PBEKeySpec(key, salt, 65536, 128);
            SecretKey tmp = null;
            tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    @NotNull
    private IvParameterSpec createIv(byte[] ivBytes) {
        return new IvParameterSpec(ivBytes);

    }


}
