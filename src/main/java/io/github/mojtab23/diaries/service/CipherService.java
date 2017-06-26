package io.github.mojtab23.diaries.service;

import jetbrains.exodus.ArrayByteIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mojtab23 on 6/3/2017.
 */

@Service
public class CipherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CipherService.class);

    //    private SecretKeyFactory factory;
    private Cipher cipher;
    private byte[] key;

    @PostConstruct
    public void init() {
        try {
//            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }


    public void setKey(byte[] key) {
        this.key = key;
    }

    public ArrayByteIterable encrypt(ArrayByteIterable value, byte[] ivBytes) {
        if (key == null) {
            LOGGER.error("key is null!");
            return null;
        } else {
            final byte[] encrypt = encrypt(key, value.getBytesUnsafe(), ivBytes);
            return new ArrayByteIterable(encrypt);
        }
    }

    public byte[] encrypt(byte[] key, byte[] value,/* byte[] salt,*/ byte[] ivBytes) {
        try {

            final SecretKeySpec secret = createSecretKey(key/*, salt*/);
            final IvParameterSpec iv = createIv(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
            return cipher.doFinal(value);


        } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayByteIterable decrypt(ArrayByteIterable value, byte[] ivBytes) {
        if (key == null) {
            LOGGER.error("key is null!");
            return null;
        } else {
            final byte[] decrypt = decrypt(key, value.getBytesUnsafe(), ivBytes);
            return new ArrayByteIterable(decrypt);
        }
    }

    public byte[] decrypt(byte[] key, byte[] value, /*byte[] salt,*/ byte[] ivBytes) {


        try {
            final SecretKeySpec secret = createSecretKey(key/*, salt*/);
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
    private SecretKeySpec createSecretKey(byte[] key/*, byte[] salt*/) {
//        try {
//
//            KeySpec spec = new PBEKeySpec(key, salt, 65536, 128);
//            SecretKey tmp;
//            tmp = factory.generateSecret(spec);
//            return new SecretKeySpec(tmp.getEncoded(), "AES");
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return null;
        return new SecretKeySpec(key, "AES");
    }

    @NotNull
    private IvParameterSpec createIv(byte[] ivBytes) {
        return new IvParameterSpec(ivBytes);

    }


}
