package com.wyh.happyyousdk.crypto;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.wyh.happyyousdk.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHelper {

    /*public static void main(String args[]) throws Exception {
        String input = "Text to encryp";
        System.out.println("input:" + input);
        String encriptedValue = encrypt(input);
        System.out.println("cipher:" + encriptedValue);
        //encriptedValue = "n0c0KYNl+PM6cekV7YMHI5b0fLodT3jU27cHt7+VGrA=";
        System.out.println("cipher:" + encriptedValue);
        String decriptedValue = decrypt(encriptedValue);
        System.out.println("output:" + decriptedValue);

    }*/

    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    String SecretKey;//32 char secret key
    String ivKey;//16 char secret key


    public CryptoHelper(Context applicationContext) {

        String ivKeyStr = applicationContext.getResources().getString(R.string.azby_k);
        ivKey = decryptData(ivKeyStr);
        ivspec = new IvParameterSpec(ivKey.getBytes());
        String secretKeyStr = applicationContext.getResources().getString(R.string.azby_s);
        SecretKey = decryptData(secretKeyStr);
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String encrypt(Context applicationContext, String valueToEncrypt) throws Exception {
        CryptoHelper enc = new CryptoHelper(applicationContext);
        // return Base64.encodeBase64String(enc.encryptInternal(valueToEncrypt));
        return Base64.encodeToString(enc.encryptInternal(valueToEncrypt), Base64.DEFAULT);
    }

    public static String decrypt(Context applicationContext, String valueToDecrypt) throws Exception {
        CryptoHelper enc = new CryptoHelper(applicationContext);
        return new String(enc.decryptInternal(valueToDecrypt));
    }

    private byte[] encryptInternal(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    private byte[] decryptInternal(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(Base64.decode(code, Base64.DEFAULT));
            //decrypted = cipher.doFinal(Base64.decodeBase64(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public String decryptData(String plainTextDecrypt) {
        byte[] data = new byte[0];
        String text = null;
        try {
            data = Base64.decode(plainTextDecrypt, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

}
