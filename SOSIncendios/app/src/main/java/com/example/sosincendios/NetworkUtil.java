package com.example.sosincendios;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtil {
   
   
   public static boolean isConnected(Context context) {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      
      if (cm != null) {
         NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
         return capabilities != null && (
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                         capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                         capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
      }
      return false;
   }
}


