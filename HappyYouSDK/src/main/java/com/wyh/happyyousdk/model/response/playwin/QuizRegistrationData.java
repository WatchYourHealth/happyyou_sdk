package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;

import java.io.Serializable;

public class QuizRegistrationData implements Serializable {

    @SerializedName("dialogDetail")
    @Expose
    DialogModel dialogModel;

    public DialogModel getDialogModel() {
        return dialogModel;
    }

    public void setDialogModel(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }
}
