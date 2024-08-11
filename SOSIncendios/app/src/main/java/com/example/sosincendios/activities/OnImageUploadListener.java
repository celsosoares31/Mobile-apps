package com.example.sosincendios.activities;

public interface OnImageUploadListener {
   void onSuccess(String downloadUrl);
   void onFailure(Exception exception);
}
