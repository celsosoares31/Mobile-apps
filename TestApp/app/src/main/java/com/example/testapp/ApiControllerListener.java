package com.example.testapp;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import video.api.player.ApiVideoPlayerController;
import video.api.player.views.ApiVideoExoPlayerView;

public class ApiControllerListener implements ApiVideoPlayerController.Listener {
   
   @Override
   public void onError(@NonNull Exception e) {
      Log.e(TAG, "An error occurred", e);
      
   }
   
   @Override
   public void onEnd() {
   
   }
   
   @Override
   public void onFirstPlay() {
   
   }
   
   @Override
   public void onPause() {
   
   }
   
   @Override
   public void onPlay() {
   
   }
   
   @Override
   public void onReady() {
      Log.i(TAG, "Player is ready");
   }
   
   @Override
   public void onSeek() {
   
   }
   
   @Override
   public void onVideoSizeChanged(@NonNull Size size) {
   
   }
}
