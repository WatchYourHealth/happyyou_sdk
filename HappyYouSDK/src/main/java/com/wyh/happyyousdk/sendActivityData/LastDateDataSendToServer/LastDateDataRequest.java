package com.wyh.happyyousdk.sendActivityData.LastDateDataSendToServer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.model.LastDateData;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class LastDateDataRequest extends AsyncTask<String, Void, ArrayList<LastDateData>> {
    private Context mContext;
    ArrayList<LastDateData> listOfItems;
    LastDataDateSynced mlastDataDateSynced;
    boolean allowHeader;

    public LastDateDataRequest(Context context, boolean allowHeader, LastDataDateSynced lastDataDateSynced) {
        mContext = context;
        this.allowHeader = allowHeader;
        listOfItems = new ArrayList<>();
        mlastDataDateSynced = lastDataDateSynced;
    }

    protected void onPreExecute() {
    }

    protected ArrayList<LastDateData> doInBackground(String... arg0) {

        String baseUrl;
        baseUrl = CommonUtils.getBaseUrlForAPI(mContext);
        String urlStr = baseUrl + "Trends/FetchRecentTrendsDate";

        SharedPreference.init(mContext);

        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            if (allowHeader) {
                conn.setRequestProperty("Authorization", SharedPreference.getAuthToken());
            }

            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();

                String finalJson = sb.toString();
                JSONObject jsonObject = new JSONObject(finalJson);
                String rewards = jsonObject.getString("rewards");

                if (rewards != null && !rewards.isEmpty()) {

                }

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                //Log.i("Json object", finalJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemJson = jsonArray.getJSONObject(i);
                    LastDateData lastDateData = new LastDateData();
                    lastDateData.setId(itemJson.getInt("id"));
                    lastDateData.setUuid(itemJson.getString("uuid"));
                    lastDateData.setTrendType(itemJson.getString("trendType"));
                    lastDateData.setTimeStamp(itemJson.getString("timeStamp"));
                    listOfItems.add(lastDateData);
                }
                return listOfItems;
            }
        } catch (Exception e) {
            Log.v("Json Exception", e.getMessage());
            //return new String("Exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<LastDateData> lastDateData) {
        super.onPostExecute(lastDateData);
        if (mlastDataDateSynced != null) {
            mlastDataDateSynced.onLastDateDataCompleted(mContext, lastDateData);
        }
    }
}
