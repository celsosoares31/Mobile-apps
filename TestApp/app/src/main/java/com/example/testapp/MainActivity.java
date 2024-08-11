package com.example.testapp;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import video.api.player.ApiVideoPlayerController;
import video.api.player.models.VideoOptions;
import video.api.player.models.VideoType;

public class MainActivity extends AppCompatActivity {
   private ApiVideoPlayerController playerView;
   Context ctx;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EdgeToEdge.enable(this);
      setContentView(R.layout.activity_main);
      ctx = getApplicationContext();
      
  }
}