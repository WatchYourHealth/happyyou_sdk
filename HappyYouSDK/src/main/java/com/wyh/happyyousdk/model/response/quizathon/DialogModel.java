package com.wyh.happyyousdk.model.response.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DialogModel implements Serializable {
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("icon")
    @Expose
    String icon;
    @SerializedName("message_top")
    @Expose
    String message_top;
    @SerializedName("message_bottom")
    @Expose
    String message_bottom;
    @SerializedName("button_text")
    @Expose
    String button_text;

    public DialogModel(String type, String title, String icon, String message_top, String message_bottom, String button_text) {
        this.type = type;
        this.title = title;
        this.icon = icon;
        this.message_top = message_top;
        this.message_bottom = message_bottom;
        this.button_text = button_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage_top() {
        return message_top;
    }

    public void setMessage_top(String message_top) {
        this.message_top = message_top;
    }

    public String getMessage_bottom() {
        return message_bottom;
    }

    public void setMessage_bottom(String message_bottom) {
        this.message_bottom = message_bottom;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }
}
