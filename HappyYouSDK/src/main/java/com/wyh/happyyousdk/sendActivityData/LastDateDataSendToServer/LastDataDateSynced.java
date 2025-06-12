package com.wyh.happyyousdk.sendActivityData.LastDateDataSendToServer;

import android.content.Context;

import com.wyhsdk.model.LastDateData;

import java.util.ArrayList;

public interface LastDataDateSynced {
    void onLastDateDataCompleted(Context context, ArrayList<LastDateData> lastDateData);

    void onLastDateDataError(String error);

}
