package com.example.sosincendios;

import com.google.firebase.appcheck.FirebaseAppCheck;

public class AppCheckTokenManager {
   private static AppCheckTokenManager instance;
   private String token;
   private boolean isRequestingToken = false;
   
   private AppCheckTokenManager() {
      // Private constructor to prevent instantiation
   }
   
   public static synchronized AppCheckTokenManager getInstance() {
      if (instance == null) {
         instance = new AppCheckTokenManager();
      }
      return instance;
   }
   
   public synchronized void getAppCheckToken(final TokenCallback callback) {
      if (token != null) {
         // Token already retrieved
         callback.onTokenRetrieved(token);
         return;
      }
      
      if (isRequestingToken) {
         // Token request is already in progress
         return;
      }
      
      isRequestingToken = true;
      
      // Make the request to get the App Check token
      FirebaseAppCheck.getInstance().getAppCheckToken(false)
              .addOnSuccessListener(appCheckToken -> {
                 token = appCheckToken.getToken();
                 isRequestingToken = false;
                 callback.onTokenRetrieved(token);
              })
              .addOnFailureListener(e -> {
                 isRequestingToken = false;
                 callback.onTokenFailure(e);
              });
   }
   
   public interface TokenCallback {
      void onTokenRetrieved(String token);
      void onTokenFailure(Exception e);
   }
}