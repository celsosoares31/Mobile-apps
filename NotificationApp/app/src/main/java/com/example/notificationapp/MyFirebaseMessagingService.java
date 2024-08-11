package com.example.notificationapp;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.app.NotificationManager;
import android.app.Service;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 *
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
   private static final String TAG = "My app";
   
   @Override
   public void onMessageReceived(RemoteMessage remoteMessage) {
      handleNow(remoteMessage.getNotification()
              .getTitle(), remoteMessage.getNotification()
              .getBody());
   }
   
   private void handleNow(String title, String body) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify");
      builder.setSmallIcon(R.drawable.ic_notification)
              .setContentTitle(title)
              .setContentText(body)
              .setAutoCancel(true);
      
      NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
      
      managerCompat.notify(102, builder.build());
   }
   
}
