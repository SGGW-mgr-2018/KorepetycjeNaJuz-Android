package pl.dawidkulpa.serverconnectionmanager;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
    public static SecretKey randomKey(int size){

        Random r= new Random();
        int len= r.nextInt(10)+15;
        StringBuilder sb= new StringBuilder();

        for(int i=0; i<len; i++){
            sb.append(r.nextInt(90)+32);
        }
        String pass= sb.toString();

        byte[] salt= new byte[32];
        new SecureRandom().nextBytes(salt);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(pass.toCharArray(), salt, 1024, size);
            SecretKey tmp= skf.generateSecret(ks);
            SecretKey key=new SecretKeySpec(tmp.getEncoded(), "AES");

            Log.e("AES key: ", new String(key.getEncoded(), "UTF-8")+"|END");

            return key;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeySpecException e){
            Log.e("AES key", e.getMessage());
            return null;
        }
    }

    public static byte[] encrypt(SecretKey key, String m){
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(m.getBytes());
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e){
            return null;
        }
    }
}
