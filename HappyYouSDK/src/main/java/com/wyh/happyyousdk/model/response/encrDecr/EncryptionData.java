package com.wyh.happyyousdk.model.response.encrDecr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EncryptionData {
    @SerializedName("encryptedStr")
    @Expose
    private String encryptedStr;
    @SerializedName("decryptedStr")
    @Expose
    private String decryptedStr;

    public String getEncryptedStr() {
        return encryptedStr;
    }

    public void setEncryptedStr(String encryptedStr) {
        this.encryptedStr = encryptedStr;
    }

    public String getDecryptedStr() {
        return decryptedStr;
    }

    public void setDecryptedStr(String decryptedStr) {
        this.decryptedStr = decryptedStr;
    }
}
