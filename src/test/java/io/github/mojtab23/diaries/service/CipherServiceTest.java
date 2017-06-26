package io.github.mojtab23.diaries.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mojtab23 on 6/24/2017.
 */
public class CipherServiceTest {


    @Test
    public void testEncrypt() throws Exception {
        CipherService cipherService = new CipherService();
        cipherService.init();

        byte[] key = "Bar12345Bar12345".getBytes("UTF-8");
        byte[] wrongKey = "Aar12345Bar12345".getBytes("UTF-8");
        final String s1 = "1";
        byte[] value = s1.getBytes("UTF-8");
        byte[] initVector = "RandomInitVector".getBytes("UTF-8");

        final byte[] encrypt = cipherService.encrypt(key, value, initVector);
        final byte[] bytes = cipherService.decrypt(key, encrypt, initVector);
//        final byte[] bytes = cipherService.decrypt(wrongKey, encrypt, initVector);
        final String s = new String(bytes, "UTF-8");
        System.out.println("initial: "+s1);
        System.out.println("last:    "+s);
        Assert.assertEquals(s,s1);


    }
}