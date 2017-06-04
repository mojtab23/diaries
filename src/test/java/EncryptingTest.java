import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

/**
 * Created by mojtab23 on 6/3/2017.
 */
public class EncryptingTest {


//    private static byte[] getUTF8Bytes(String input) {
//        return input.getBytes(StandardCharsets.UTF_8);
//    }
//
//    @Test
//    public void jasyptTest() throws Exception {
//
//        final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("mojtab23"), "AES");
//        final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
//        Properties properties = new Properties();
//        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CipherProvider.JCE.getClassName());
//        //Creates a CryptoCipher instance with the transformation and properties.
//        final String transform = "AES/CBC/PKCS5Padding";
//        CryptoCipher encipher = Utils.getCipherInstance(transform, properties);
//        System.out.println("Cipher:  " + encipher.getClass().getCanonicalName());
//
//        final String sampleInput = "hello world!";
//        System.out.println("input:  " + sampleInput);
//
//        byte[] input = getUTF8Bytes(sampleInput);
//        byte[] output = new byte[32];
//
//        //Initializes the cipher with ENCRYPT_MODE, key and iv.
//        encipher.init(Cipher.ENCRYPT_MODE, key, iv);
//        //Continues a multiple-part encryption/decryption operation for byte array.
//        int updateBytes = encipher.update(input, 0, input.length, output, 0);
//        System.out.println(updateBytes);
//        //We must call doFinal at the end of encryption/decryption.
//        int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
//        System.out.println(finalBytes);
//        //Closes the cipher.
//        encipher.close();
//
//        System.out.println(Arrays.toString(Arrays.copyOf(output, updateBytes + finalBytes)));
//
//        // Now reverse the process using a different implementation with the same settings
//        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CipherProvider.JCE.getClassName());
//        CryptoCipher decipher = Utils.getCipherInstance(transform, properties);
//        System.out.println("Cipher:  " + encipher.getClass().getCanonicalName());
//
//        decipher.init(Cipher.DECRYPT_MODE, key, iv);
//        byte[] decoded = new byte[32];
//        decipher.doFinal(output, 0, updateBytes + finalBytes, decoded, 0);
//
//        System.out.println("output: " + new String(decoded, StandardCharsets.UTF_8));
//
//    }
//
//    private byte[] createKey(String key, byte[] salt) {
//        try {
//            KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 200_000, 256); // AES-256
//            SecretKeyFactory f = null;
//
//            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//
//            return f.generateSecret(spec).getEncoded();
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Test
    public void finalTest() throws Exception {
        String initVector = "RandomInitVector"; // 16 bytes IV
        String password = "mojtabdfsafffffffffffffffffffffffffffffffffffffffffffffffffffffffff23";
        final byte[] bytes = initVector.getBytes("UTF-8");
        final byte[] salt = initVector.substring(8).getBytes("UTF-8");

        IvParameterSpec iv = new IvParameterSpec(bytes);


        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
        byte[] ciphertext = cipher.doFinal("Hello, Mojtaba World!".getBytes("UTF-8"));
        System.out.println("size:" + ciphertext.length + " value:" + ciphertext);


        /* Decrypt the message, given derived key and initialization vector. */
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, iv);
        String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
        System.out.println(plaintext);


    }
}
