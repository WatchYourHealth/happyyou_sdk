package com.wyh.happyyousdk.APIEncryption;

import android.util.Base64;

import com.wyh.happyyousdk.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

    String ivKey;
    String SecretKey;
    private SecretKeySpec keyspec;
    private IvParameterSpec ivspec;
    private Cipher cipher;


    public String encryptMsg(String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        try {
            generateKey();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }

    private byte[] decryptInternal(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(Base64.decode(code, Base64.NO_WRAP));
            //decrypted = cipher.doFinal(Base64.decodeBase64(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public String decrypt(String valueToDecrypt) throws Exception {
        AESEncryption enc = new AESEncryption();
        return new String(enc.decryptInternal(valueToDecrypt));
    }

    public void generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        ivKey = decryptData(Constants.i_v_ke_y);
        ivspec = new IvParameterSpec(ivKey.getBytes());

        SecretKey = decryptData(Constants.hidden_ke_y);
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

    public String decryptData(String plainTextDecrypt) {
        byte[] data = new byte[0];
        String text = null;
        try {
            data = android.util.Base64.decode(plainTextDecrypt, android.util.Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

}
