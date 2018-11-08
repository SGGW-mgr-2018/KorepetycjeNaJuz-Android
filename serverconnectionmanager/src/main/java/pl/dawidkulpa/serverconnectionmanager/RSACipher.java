package pl.dawidkulpa.serverconnectionmanager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACipher {
    public static PrivateKey readPrivateKey(Context context, String path){
        try {
            InputStream is = context.getAssets().open(path);
            byte[] keyBytes = new byte[is.available()];
            is.read(keyBytes);
            is.close();

            PKCS8EncodedKeySpec spec= new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e){
            return null;
        }
    }

    public static PublicKey readPublicKey(Context context, String path){
        try {
            InputStream is = context.getAssets().open(path);
            byte[] keyBytes = new byte[is.available()];
            is.read(keyBytes);
            is.close();

            X509EncodedKeySpec spec= new X509EncodedKeySpec(keyBytes);
            Log.e("Len", new String(keyBytes, "UTF-8"));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e){
            Log.e("RSACipher", "Read public key: "+e.getMessage());
            return null;
        }
    }

    public static byte[] Encrypt(PublicKey k, byte[] m){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, k);

            return cipher.doFinal(m);
        } catch (NoSuchAlgorithmException |NoSuchPaddingException |InvalidKeyException |IllegalBlockSizeException |BadPaddingException e){
            Log.e("RSACipher", "Encrypt: "+e.getMessage());
            return null;
        }
    }

    public static String Decrypt(PrivateKey k, byte[] s){
        try {
            Cipher cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, k);

            return new String(cipher.doFinal(s));
        } catch (NoSuchAlgorithmException |NoSuchPaddingException |InvalidKeyException |IllegalBlockSizeException |BadPaddingException e){
            return null;
        }
    }
}
