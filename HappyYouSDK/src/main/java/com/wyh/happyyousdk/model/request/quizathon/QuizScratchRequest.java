package com.wyh.happyyousdk.model.request.quizathon;

import java.io.Serializable;

public class QuizScratchRequest implements Serializable {
    private String TransId;
    private boolean IsScratched;

    public QuizScratchRequest(String transId, boolean isScratched) {
        TransId = transId;
        IsScratched = isScratched;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public boolean isScratched() {
        return IsScratched;
    }

    public void setScratched(boolean scratched) {
        IsScratched = scratched;
    }
}
