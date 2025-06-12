package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DialogModel implements Serializable {

    @SerializedName("dailogType")
    @Expose
    public String type;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("messageTop")
    @Expose
    public String message_top;
    @SerializedName("messageBottom")
    @Expose
    public String message_bottom;
    @SerializedName("buttonText")
    @Expose
    public String button_text;
    @SerializedName("buttonTextOptional")
    @Expose
    public String button_text_optional;
    @SerializedName("redirectionKey")
    @Expose
    public String redirectionKey;

    public String getRedirectionKey() {
        return redirectionKey;
    }

    public void setRedirectionKey(String redirectionKey) {
        this.redirectionKey = redirectionKey;
    }

    public String getButton_text_optional() {
        return button_text_optional;
    }

    public void setButton_text_optional(String button_text_optional) {
        this.button_text_optional = button_text_optional;
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
