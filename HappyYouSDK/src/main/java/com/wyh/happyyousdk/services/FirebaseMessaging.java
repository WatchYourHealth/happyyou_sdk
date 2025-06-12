/*
package com.wyh.happyyousdk.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SplashScreenActivity;
import com.wyh.happyyousdk.utils.SharedPref;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class FirebaseMessaging extends FirebaseMessagingService {

    String redirectionURL = "";
    String vendorLogo = "";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPref.init(getApplicationContext());
        SharedPref.putPushNotificationToken(token);
        Log.v("New Firebase Token", token);
        Log.d("AuthToken","New Firebase "+token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            SharedPref.init(getApplicationContext());
            processNotification(remoteMessage);
        } catch (Throwable e) {
            e.printStackTrace();
            processNotification(remoteMessage);
        }

    }

    private void processNotification(RemoteMessage remoteMessage) {
        Map<String, String> data1 = remoteMessage.getData();
        String data2 = "";
        if (data1.containsKey("NotificationType")) {
            data2 = data1.get("NotificationType");
        }
        String id;
        String bodyMessage = "";
        String title = "";
        String notificationImageUrl = "";


        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getImageUrl() != null) {
            notificationImageUrl = remoteMessage.getNotification().getImageUrl().toString();
        }
        Log.d("NotifyVisitors", notificationImageUrl);
        Log.d("AuthToken","Notification "+ data2);

//        Log.d("NotificationMessage id", ""+remoteMessage.getNotification());
        if (remoteMessage.getNotification() != null) {
            id = remoteMessage.getData().get("CommunityId");
            bodyMessage = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
            if (data1.containsKey("CommunityId") && id == null)
                id = data1.get("CommunityId");
            Log.d("NotificationMessage id", ""+id);
            sendNotification(data2, id, bodyMessage, title, notificationImageUrl, 0);
        }
        if (remoteMessage.getNotification() != null) {
            redirectionURL = remoteMessage.getData().get("RedirectionUrl");
            vendorLogo = remoteMessage.getData().get("VendorLogo");
            bodyMessage = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
            if(data2 != null && data2.equalsIgnoreCase("hobby tribe")){
                Log.d("AuthToken","Hobby Tribe Out Data");
                SharedPref.putHobbyTribeLogo(vendorLogo);
                SharedPref.putHobbyTribeUrl(redirectionURL);
            }

            if (data1.containsKey("RedirectionUrl") && redirectionURL == null)
                redirectionURL = data1.get("RedirectionUrl");

            if (data1.containsKey("VendorLogo") && vendorLogo == null)
                vendorLogo = data1.get("VendorLogo");

            sendNotification(data2, "", bodyMessage, title, notificationImageUrl, 0);
        }
        if (data2 != null && (data2.equalsIgnoreCase("Tribe Message") || data2.equalsIgnoreCase("Tribe Update"))) {

            Intent intent = new Intent("refresh");
            //You can also include some extra data.
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private void sendNotification(String obj, String tribeId, String body, String title, String imageUrl, int openTab) {
        Log.v("intentType_is", "" + obj);
        Log.d("intentType_is", "" + tribeId);
       */
/* if("CHOOSETOLOSE".equalsIgnoreCase(obj) || "CHOOSETOLOSE".equalsIgnoreCase(body))
        Analytics.logEvent(this, "FirebaseMessaging", getString(R.string.choose_to_lose_notification_received), "", "");
*//*

        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.putExtra("notification_type", obj);
        intent.putExtra("CommunityId", tribeId);
        intent.putExtra("redirectionURL", redirectionURL);
        intent.putExtra("vendorLogo", vendorLogo);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String channelId = getString(R.string.android_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        if (imageUrl != null && !imageUrl.isEmpty()){
            Bitmap bitmap = getBitmapfromUrl(imageUrl);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Kotak",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        */
/*if (imageUrl != null && imageUrl.length() != 0) {
            Bitmap bitmap = getBitmapfromUrl(imageUrl);
            notificationBuilder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
            ).setLargeIcon(bitmap);
        }*//*


        notificationManager.notify(1  */
/*ID of notification*//*
, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
//            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }

}
*/
