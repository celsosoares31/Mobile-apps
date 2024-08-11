package com.example.sosincendios;

import static android.app.Activity.RESULT_OK;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
   private  final String CHANNEL_ID = "notification_channel";
   private final String CHANNEL_NAME = "com.example.sosincendios";
   private final long[] VIBRATION_PATTERN = {1000,1000,1000,1000};
   //   Generate the notification
   //   Attach the notification created with the custom layout
   
//   @Override
//   public void onNewToken(@NonNull String token) {
//      Log.d("onNewToken", "Refreshed token: " + token);
//
//      // If you want to send messages to this application instance or
//      // manage this apps subscriptions on the server side, send the
//      // FCM registration token to your app server.
//      sendRegistrationToServer(token);
//   }
   
   @Override
   public void onMessageReceived(@NonNull RemoteMessage message) {
      if(message.getNotification() != null){
         generateNotification(message.getNotification().getTitle(),
                 message.getNotification().getBody());
      }
   }
   
   
   public void generateNotification(String title, String message){
      Intent intent = new Intent(this, MainActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      
      PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
      NotificationCompat.Builder builder =
              new NotificationCompat.Builder(getApplicationContext(),
                      CHANNEL_ID);
      
      builder.setSmallIcon(R.drawable.logo_simples)
              .setAutoCancel(true)
              .setVibrate(VIBRATION_PATTERN)
              .setOnlyAlertOnce(true)
              .setContentIntent(pendingIntent);
      builder = builder.setContent(getRemoteView(title, message));
      
      NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
         NotificationChannel notificationChannel =
                 new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                         notificationManager.getImportance());
         notificationManager.createNotificationChannel(notificationChannel);
      }
      notificationManager.notify(0,builder.build());
   }
   
   private RemoteViews getRemoteView(String title, String message){
      RemoteViews remoteViews = new RemoteViews(CHANNEL_NAME,
              R.layout.notification);
      remoteViews.setTextViewText(R.id.notificationTitle, title);
      remoteViews.setTextViewText(R.id.notificationDescription, message);
      remoteViews.setImageViewResource(R.id.notificationLogo, R.mipmap.ic_launcher);
      return remoteViews;
   }}