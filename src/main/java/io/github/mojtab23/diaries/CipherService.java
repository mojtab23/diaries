package io.github.mojtab23.diaries;

import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.cipher.CryptoCipherFactory;
import org.apache.commons.crypto.utils.Utils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Properties;

/**
 * Created by mojtab23 on 5/26/2017.
 */

@Service
public class CipherService {


    private final static String TRANSFORM = "AES/CBC/PKCS5Padding";
    //    private volatile byte[] userKey = new byte[0];
    private volatile boolean isOpen = false;
    private CryptoCipher encipher;
    private SecretKeySpec key;
    private IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("mojtaba_zarezade"));
    private Properties properties;

    /**
     * Converts String to UTF8 bytes
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] encrypt(byte[] input) {
        try {
            byte[] output = new byte[32];

            encipher.init(Cipher.ENCRYPT_MODE, key, iv);

            //Continues a multiple-part encryption/decryption operation for byte array.
            int updateBytes = encipher.update(input, 0, input.length, output, 0);
            System.out.println(updateBytes);
            //We must call doFinal at the end of encryption/decryption.
            int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
            //Closes the cipher.
            encipher.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }
    }

    public byte[] decrypt(byte[] input) {
        try {
            // Now reverse the process using a different implementation with the same settings
//            properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CryptoCipherFactory.CipherProvider.JCE.getClassName());
            CryptoCipher decipher = Utils.getCipherInstance(TRANSFORM, properties);
            System.out.println("Cipher:  " + encipher.getClass().getCanonicalName());

            decipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decoded = new byte[32];
            decipher.doFinal(output, 0, updateBytes + finalBytes, decoded, 0);

            System.out.println("output: " + new String(decoded, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (ShortBufferException e) {

            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public boolean open(byte[] userKey) throws IOException {
        key = new SecretKeySpec(userKey, "AES");
        properties = new Properties();
        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CryptoCipherFactory.CipherProvider.OPENSSL.getClassName());
        //Creates a CryptoCipher instance with the transformation and properties.

        encipher = Utils.getCipherInstance(TRANSFORM, properties);


    }

    public void close() {

    }


}
