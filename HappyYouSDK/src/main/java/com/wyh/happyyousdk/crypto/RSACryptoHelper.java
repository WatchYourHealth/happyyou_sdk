package com.wyh.happyyousdk.crypto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.*;
import java.util.Base64;

public class RSACryptoHelper {

    public RSACryptoHelper() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void encryptTest(){
        KeyPairGenerator keyPairGenerator =
                null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom();

            keyPairGenerator.initialize(2048, secureRandom);

            KeyPair pair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = pair.getPublic();

            String publicKeyString =
                    Base64.getEncoder().encodeToString(publicKey.getEncoded());
            publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzltuwAP8HP/gGDKi0AS9q4A2xufc4WhIb0I16ZrZKOUc1BLtnLXq3n3+NkC3imXbQavcpe0x+mwIXzXV3o0R/QdruWatV77Y15bTS2jxXfT1B6AM2GAAtMev22LU8z0Mr+eU0kKNGaUzM2cYdo/Ys8+RCTODFCDeiRAOhngfEIQIDAQAB";

            System.out.println("public key = " + publicKeyString);

            PrivateKey privateKey = pair.getPrivate();

            String privateKeyString =
                    Base64.getEncoder().encodeToString(privateKey.getEncoded());

            privateKeyString = "MIICXAIBAAKBgQCzltuwAP8HP/gGDKi0AS9q4A2xufc4WhIb0I16ZrZKOUc1BLtnLXq3n3+NkC3imXbQavcpe0x+mwIXzXV3o0R/QdruWatV77Y15bTS2jxXfT1B6AM2GAAtMev22LU8z0Mr+eU0kKNGaUzM2cYdo/Ys8+RCTODFCDeiRAOhngfEIQIDAQABAoGAc882wBsEWpJ/yLblG58gCtWNPyzjJa2/u7wTX/lYWCjzqFby2qhTWJI8WPe+/z9jhSdVV6SxQXuIL/1IYOwiQLGO8w8vODpEl1/yp3Xdp9ceoRZFhYi8CMs6wJ8uFGa3MvRo1Na3ipIhiIQE5PZVR30cwNJv+hSflN6382IrM9kCQQDZj8Qk1HvUhMYcSY3nYCnzn4OjOmS69FaR1oXw4frAXDIlSvtxNK4a4VoFtPg2dgB0hrYZmcABBrPjrTZ8VJavAkEA01GeTX3MhDM7Wair1l8FRml3V7RIxDH0aIDaSAmbp7vyHZuudVYjp2cuIKMA8TyV+sdJc7xo76Ez+YBV3ogGLwJBAM7ZV53EwG9QRaODc322+mv9D4VT8Y2m3eAO3I4n7OaTQcIfRm1druMzuwmOBCiTjN89bUlt2xWer84aU6FmM1UCQFq9CDmEFAaMLSbxq1+wi4ZRkSJC0qAURzt8YL5gCct7g5o9GCtgIo0mXAM9mxD7D/eQDTU6KOEEmlm/Rp/4TpcCQDlSN9Hwy6e4TQu3sSdYCmHElfPLB6733E4NU0sbUpYpXRGkAtT0/Plct5uZ3q4BHPr+7KFZdaEQRMetDLxC/JQ=";

            System.out.println("private key = " + privateKeyString);

            //Encrypt Hello world message
            Cipher encryptionCipher = Cipher.getInstance("RSA");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, privateKey);
            String message = "Hello world";
            byte[] encryptedMessage =
                    encryptionCipher.doFinal(message.getBytes());
            String encryption =
                    Base64.getEncoder().encodeToString(encryptedMessage);
            System.out.println("encrypted message = " + encryption);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

}